package cn.wizzer.bugwk.commons.service;

import cn.wizzer.bugwk.commons.utils.LuceneIndex;
import cn.wizzer.bugwk.modles.Bug;
import cn.wizzer.bugwk.modles.Reply;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.nutz.aop.interceptor.async.Async;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2018.08
 */
@IocBean(create = "init", depose = "close")
public class SearchService {
    private static Log log = Logs.get();

    @Inject("java:$conf.get('lucene.dir')")
    private String indexDir;
    @Inject
    protected Dao dao;

    protected LuceneIndex luceneIndex;

    @Async
    public void addIndex(Bug bug, boolean add) {
        _add(bug, add);
    }

    protected void _add(Bug bug, boolean add) {
        if (bug == null)
            return;
        Document document = new Document();
        Field field;
        FieldType fieldType;
        dao.fetchLinks(bug, null);
        // 先加入id
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS);// 索引
        fieldType.setStored(true);// 存储
        fieldType.setTokenized(false);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPayloads(true);
        fieldType.setStoreTermVectorPositions(true);// 存储位置
        fieldType.setStoreTermVectorOffsets(true);// 存储偏移量
        field = new Field("id", bug.getId(), fieldType);
        document.add(field);

        // 加入标题
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS);// 索引
        fieldType.setStored(true);// 存储
        fieldType.setStoreTermVectors(true);
        fieldType.setTokenized(true);
        fieldType.setStoreTermVectorPositions(true);// 存储位置
        fieldType.setStoreTermVectorOffsets(true);// 存储偏移量
        field = new Field("title", bug.getTitle(), fieldType);
        document.add(field);

        // 加入文章内容
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS);// 索引
        fieldType.setStored(false);// 存储
        fieldType.setStoreTermVectors(true);
        fieldType.setTokenized(true);
        fieldType.setStoreTermVectorPositions(true);// 存储位置
        fieldType.setStoreTermVectorOffsets(true);// 存储偏移量
        field = new Field("note", bug.getNote(), fieldType);
        document.add(field);

        StringBuilder sb = new StringBuilder();
        if (bug.getReplies() != null) {
            for (Reply reply : bug.getReplies()) {
                if (reply == null)
                    continue;
                if (reply.getNote() != null) {
                    if (sb.length() + reply.getNote().length() > (IndexWriter.MAX_TERM_LENGTH / 4)) {
                        break;
                    }
                    sb.append(reply.getNote());
                }
            }
        }
        fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS);// 索引
        fieldType.setStored(false);// 存储
        fieldType.setStoreTermVectors(true);
        fieldType.setTokenized(true);
        fieldType.setStoreTermVectorPositions(true);// 存储位置
        fieldType.setStoreTermVectorOffsets(true);// 存储偏移量

        field = new Field("reply", sb.toString(), fieldType);
        document.add(field);

        try {
            luceneIndex.writer.updateDocument(new Term("id", bug.getId()), document);
            luceneIndex.writer.commit();
        } catch (Exception e) {
            log.debug("add to index fail : id=" + bug.getId());
        } catch (Error e) {
            log.debug("add to index fail : id=" + bug.getId());
        }
    }

    public void rebuild() throws IOException {
        Sql sql = Sqls.queryString("select id from bug");
        dao.execute(sql);
        luceneIndex.writer.deleteAll();
        String[] topicIds = sql.getObject(String[].class);
        for (String topicId : topicIds) {
            Bug bug = dao.fetch(Bug.class, topicId);
            _add(bug, true);
        }
        luceneIndex.writer.commit();
    }


    Map<String, Float> boosts = new HashMap<>();

    {
        boosts.put("title", 5.0f);
        boosts.put("note", 3.0f);
        boosts.put("reply", 2.0f);
    }

    String[] fields = boosts.keySet().toArray(new String[boosts.size()]);

    public List<LuceneSearchResult> search(String keyword, int size)
            throws IOException, ParseException {
        IndexReader reader = luceneIndex.reader();
        try {
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = luceneIndex.analyzer();
            MultiFieldQueryParser parser = new MultiFieldQueryParser(
                    fields,
                    analyzer,
                    boosts);
            // 将关键字包装成Query对象
            Query query = parser.parse(keyword);
            TopDocs results = searcher.search(query, size);
            List<LuceneSearchResult> searchResults = new ArrayList<LuceneSearchResult>();
            for (ScoreDoc sd : results.scoreDocs) {
                String id = searcher.doc(sd.doc).get("id");
                searchResults.add(new LuceneSearchResult(id, searcher.doc(sd.doc).get("title")));
            }
            return searchResults;
        } finally {
            reader.close();
        }
    }

    public void init() throws IOException {
        Files.createDirIfNoExists(indexDir);
        luceneIndex = new LuceneIndex(indexDir, IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    public void close() throws IOException {
        if (luceneIndex != null)
            luceneIndex.close();
    }
}

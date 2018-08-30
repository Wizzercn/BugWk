import User from '@/components/User'
import Lol from '@/components/Lol'
import Add from '@/components/Add'
import Bug from '@/components/Bug'
import Search from '@/components/Search'

export default [
    {
        path: "/",
        redirect: "lol"
    },
    {
        path: "/user",
        name: 'user',
        component: User
    },
    {
        path: "/lol",
        name: 'lol',
        component: Lol
    },
    {
        path: "/lol/search/:keyword",
        name: 'search',
        component: Search
    },
    {
        path: "/lol/add",
        name: 'add',
        component: Add
    },
    {
        path: "/lol/bug/:id",
        name: 'bug',
        component: Bug
    },
]
import User from '@/components/User'
import Lol from '@/components/Lol'
import Add from '@/components/Add'

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
        path: "/lol/add",
        name: 'add',
        component: Add
    },
]
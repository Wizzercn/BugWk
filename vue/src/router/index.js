import User from '@/components/User'
import Lol from '@/components/Lol'

export default [
    {
        path: "/",
        redirect:"lol"
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
]
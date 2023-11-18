import {createRouter, createWebHistory} from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/index",
        children: [
            {
                path: 'user',
                name: 'user',
                component: () => import("../views/user.vue")
            },
            {
                path: 'teacher',
                name: 'teacher',
                component: () => import("../views/teacher.vue")
            },
            {
                path: 'student',
                name: 'student',
                component: () => import("../views/student.vue")
            },
            {
                path: 'index',
                name: 'index',
                component: () => import("../views/index.vue")
            },
            {
                path: 'news',
                name: 'news',
                component: () => import("../views/news.vue")
            },
            {
                path: 'department',
                name: 'department',
                component: () => import("../views/department.vue")
            },
            {
                path: 'departShow',
                name: 'departShow',
                component: () => import("../views/departShow.vue")
            },
            {
                path: 'qgsq',
                name: 'qgsq',
                component: () => import("../views/qgsq.vue")
            },
            {
                path: 'xysh',
                name: 'xysh',
                component: () => import("../views/xysh.vue")
            },
            {
                path: 'term',
                name: 'term',
                component: () => import("../views/term.vue")
            },
            {
                path: 'print',
                name: 'print',
                component: () => import("../views/print.vue")
            },
        ]
    },
    {
        path: '/Login',
        name: 'Login',
        component: () => import("../views/Login")
    },
    {
        path: '/DepartPhone',
        name: 'DepartPhone',
        component: () => import("../views/DepartPhone")
    },
    {
        path: '/qgsqPhone',
        name: 'qgsqPhone',
        component: () => import("../views/qgsqPhone")
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router

import {createRouter, createWebHistory} from "vue-router";

const routes = [
    {
        path: '/', name: 'Home',
        component: () => import('../views/Home.vue')
    },
    {
        path: '/login', name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register', name: 'Register',
        component: () => import('../views/registration/UserRegistration.vue')
    },
    {
        path: '/supplier/registration', name: 'Register Supplier',
        component: () => import('../views/registration/SupplierRegistration.vue')
    },
    {
        path: '/company/registration', name: 'Register Company',
        component: () => import('../views/registration/CompanyRegistration.vue')
    },
    {
        path: '/company', name: 'Company Profile',
        component: () => import('../views/Company.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router

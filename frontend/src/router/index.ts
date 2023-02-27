// Composables
import {createRouter, createWebHistory} from 'vue-router'
import {useAuthStore, useCompanyStore, useUserStore} from "@/store/app";

const routes = [
  {
    path: '/', name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/login', name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/registration', name: 'Register',
    component: () => import('@/views/registration/UserRegistration.vue')
  },
  {
    path: '/supplier/registration', name: 'Register Supplier',
    component: () => import('@/views/registration/SupplierRegistration.vue')
  },
  {
    path: '/company/registration', name: 'Register Company',
    component: () => import('@/views/registration/CompanyRegistration.vue')
  },
  {
    path: '/company', name: 'Company Profile',
    component: () => import('@/views/Company.vue')
  },
  {
    path: '/profile', name: 'User Profile',
    component: () => import('@/views/Profile.vue')
  },
  {
    path: '/sysadm/companies', name: 'Companies',
    component: () => import('@/views/sysadm/Companies.vue')
  },
  {
    path: '/sysadm/suppliers', name: 'Suppliers',
    component: () => import('@/views/sysadm/Suppliers.vue')
  },
  {
    path: '/sysadm/organizations', name: 'Organizations',
    component: () => import('@/views/sysadm/Organizations.vue')
  },
  {
    path: '/sysadm/users', name: 'Users',
    component: () => import('@/views/sysadm/Users.vue')
  }
]
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

router.beforeEach(async (to, from, next) => {
  await initializeStores()
  next()
})

async function initializeStores() {
  if (!useAuthStore().hasToken)
    useAuthStore().initializeToken()
  if (useAuthStore().hasToken)
    await useUserStore().requestUserData()
  if (useUserStore().isEmployee) {
    await useCompanyStore().requestFreshCompanyData()
  }
}

export default router

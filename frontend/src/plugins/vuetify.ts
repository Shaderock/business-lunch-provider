/**
 * plugins/vuetify.ts
 *
 * Framework documentation: https://vuetifyjs.com`
 */

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import {createVuetify} from 'vuetify'
import {VDataTable} from 'vuetify/labs/VDataTable'

// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
export default createVuetify({
  components: {
    VDataTable,
  },
  defaults: {
    VDataTable: {
      fixedHeader: true,
      noDataText: 'Results not found',
    },
  },
  theme: {
    defaultTheme: 'dark',
    themes: {
      light: {
        colors: {
          primary: '#6200EE',
          secondary: '#03DAC6',
          onPrimary: '#FFFFFF',
          onSecondary: '#000000',
          surface: '#FFFFFF',
          background: '#F5F5F5',
          error: '#B00020',
          success: '#00C853',
          warning: '#FFAB00',
          info: '#2196F3',
        },
      },
      dark: {
        colors: {
          primary: '#BB86FC',
          secondary: '#03DAC6',
          onPrimary: '#000000',
          onSecondary: '#FFFFFF',
          surface: '#121212',
          background: '#121212',
          error: '#CF6679',
          success: '#00E676',
          warning: '#FFD600',
          info: '#82B1FF',
        },
      },
    },
  },
})

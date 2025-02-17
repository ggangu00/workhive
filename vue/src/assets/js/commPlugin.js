import { provide } from 'vue';
import comm from './common.js';

export default {
  install: () => {
    provide('comm', comm);
  }
};

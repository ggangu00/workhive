import { provide } from 'vue';
import comm from './common.js';
import project from './project.js';

export default {
  install: () => {
    provide('comm', comm);
    provide('project', project);
  }
};

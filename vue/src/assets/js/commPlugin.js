import { provide } from 'vue';
import comm from './common.js';
import project from './project.js';

export default {
  setup() {
    provide('comm', comm);
    provide('project', project);
  }
};

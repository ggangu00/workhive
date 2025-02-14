<template>
    <div class="content">
       <div class="container-fluid">
   
         <!--등록폼 [S]-->
         <div class="card">
           <div class="card-body">
             <h4 class="card-title float-left mt-1">게시글 목록</h4>         
           </div>
         </div>
   
         </div>
   </div>
   
   </template>

import 'tui-grid/dist/tui-grid.css';
import { Grid } from '@toast-ui/vue-grid';

export default {
  components: {
    grid: Grid
  },
  created() {
    this.gridProps = {
      data: [
        // for rowData prop
        {
          name: 'Beautiful Lies',
          artist: 'Birdy'
        },
        {
          name: 'X',
          artist: 'Ed Sheeran'
        }
      ],
      columns: [
        // for columnData prop
        {
          header: 'Name',
          name: 'name'
        },
        {
          header: 'Artist',
          name: 'artist'
        }
      ]
    };
  }
};
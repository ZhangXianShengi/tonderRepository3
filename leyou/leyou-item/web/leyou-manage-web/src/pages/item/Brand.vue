<template>

  <v-card>
    <v-card-title>
      <v-btn @click="addBrand" color="primary">新增品牌</v-btn>
      <v-spacer/>
      <v-text-field
        append-icon="search"
        label="搜索"
        single-line
        hide-details
        v-model="search"
      />
    </v-card-title>
    <v-divider/>
    <v-data-table
      :headers="headers"
      :items="items"
      :pagination.sync="pagination"
      :total-items="totalItems"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center"><img v-if="!!props.item.image" width="102" height="36" :src="props.item.image"/></td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="justify-center layout px-0">
          <v-btn icon @click="editBrand(props.item)">
            <i class="el-icon-edit"/>
          </v-btn>
          <v-btn icon @click="deleteBrand(props.item)">
            <i class="el-icon-delete"/>
          </v-btn>
        </td>
      </template>
      <template slot="expand" slot-scope="props">
        <v-card flat>
          <v-card-text>Peek-a-boo!</v-card-text>
        </v-card>
      </template>
      <template slot="no-data">
        <v-alert :value="true" color="error" icon="warning">
          对不起，没有查询到任何数据 :(
        </v-alert>
      </template>
      <template slot="pageText" slot-scope="props">
        共{{props.itemsLength}}条,当前:{{ props.pageStart }} - {{ props.pageStop }}
      </template>
    </v-data-table>

    <v-dialog v-model="show" max-width="600" scrollable v-if="show">
      <v-card>
        <v-toolbar dark dense color="primary">
          <v-toolbar-title>{{isEdit ? '修改品牌' : '新增品牌'}}</v-toolbar-title>
          <v-spacer/>
          <v-btn icon @click="show = false">
            <v-icon>close</v-icon>
          </v-btn>
        </v-toolbar>
        <v-card-text class="px-5 py-2">
          <!-- 表单 -->
          <brand-form :oldBrand="brand" :isEdit="isEdit" @close="show = false" :reload="getDataFromApi"/>
        </v-card-text>
      </v-card>
    </v-dialog>
  </v-card>

</template>

<script>
  import BrandForm from './BrandForm'
  import {brandData} from '../../mockDB'

  export default {
    name: "brand",
    components: {
      BrandForm
    },
    data() {
      return {
        search: '',// 过滤字段
        totalItems: 0,// 总条数
        items: [],// 表格数据
        loading: true,
        pagination: {},// 分页信息
        names:"a",
        headers: [// 表头
          {text: 'id', align: 'center', value: 'id'},
          {text: '名称', align: 'center', sortable: false, value: 'name'},
          {text: 'LOGO', align: 'center', sortable: false, value: 'image'},
          {text: '首字母', align: 'center', value: 'letter', sortable: true,},
          {text: '操作', align: 'center', value: 'id', sortable: false}
        ],
        show: false,// 是否弹出窗口
        brand: {}, // 品牌信息
        isEdit: false // 判断是编辑还是新增
      }
    },
    watch: {
      pagination: {
        handler() {
          deep: true,
          this.getDataFromApi();
        },
        search: {
        handler() {
          this.getDataFromApi();
        }
      }
      }
      ,
      show(val, oldVal) {
        // 表单关闭后重新加载数据
        if (oldVal) {
          this.getDataFromApi();
        }
      }
    },
    mounted() {
      this.getDataFromApi();
    },
    methods: {
      addBrand() {
        this.brand = {};
        this.isEdit = false;
        this.show = true;
      },
      editBrand(item) {
        this.brand = item;
        this.isEdit = true;
        this.show = true;
        // 查询商品分类信息，进行回显
        this.$http.get("/item/category/bid/" + item.id)
          .then(resp => {
            console.log(this.brand.name)
            console.log(resp.data)
            console.log(resp.data[0].brand.name)
            this.brand.name = resp.data[0].brand.name
          })

      },

      deleteBrand(item) {
        this.$message.confirm('此操作将永久删除该品牌, 是否继续?').then(() => {
          // 发起删除请求
          this.$http.delete("/item/brand/delete?id=" + item.id,)
            .then(() => {
              // 删除成功，重新加载数据
              this.$message.success("删除成功！");
              this.getDataFromApi();
            })
        }).catch(() => {
          this.$message.info("删除已取消！");
        });

      },
      /*this.loading = true;
               200ms后返回假数据
              window.setTimeout(() => {
              this.items = brandData.slice(0,4);
                             this.totalItems = 100
                             this.loading = false;


              }, 200)*/
      getDataFromApi() {
            // 发起请求
            this.$http.get("/item/brand/page",{
                    params:{
                        key: this.search, // 搜索条件
                        page: this.pagination.page,// 当前页
                        rows: this.pagination.rowsPerPage,// 每页大小
                        sortBy: this.pagination.sortBy,// 排序字段
                        desc: this.pagination.descending// 是否降序
                    }
                }).then(resp => { // 这里使用箭头函数
                    // 将得到的数据赋值给本地属性
                    this.items = resp.data.items;
                    this.totalBrands = resp.data.total;
                    // 完成赋值后，把加载状态赋值为false
                    this.loading = false;
                })
       }
    }
  }
</script>

<style scoped>

</style>

<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="params"
      hide-actions
      class="elevation-0">
      <template slot="items" slot-scope="props">

        <td class="text-xs-center">{{props.item.id}}</td>
        <td class="text-xs-center">{{props.item.name}}</td>
        <td class="text-xs-center">{{props.item.numerical}}</td>
        <td class="text-xs-center">{{props.item.unit || '无'}}</td>
        <td class="text-xs-center">{{props.item.generic}}</td>
        <td class="text-xs-center">{{(props.item.searching)}}</td>
        <td class="justify-center layout px-0">
          <v-btn icon @click="editParam(props.item)">
            <i class="el-icon-edit"/>
          </v-btn>
          <v-btn icon @click="deleteParam(props.item)">
            <i class="el-icon-delete"/>
          </v-btn>
        </td>
      </template>
      <template slot="no-data">
        该分组下没有参数
      </template>
    </v-data-table>
    <v-btn color='primary' @click="addParam">新增参数</v-btn>
    <v-dialog v-model="show" max-width="350px" scrollable>
      <v-card>
        <v-card-text style="">
          <v-flex class="px-3">
            <v-text-field label="参数名称:" v-model="param.name"/>
            <v-checkbox label="是否为通用属性" v-model="param.generic" color="primary" hide-details/>
            <v-checkbox label="是否为数值类型" v-model="param.numerc" color="primary" hide-details/>
            <v-text-field label="数值单位" v-model="param.unit" v-if="param.numeric"/>
            <v-checkbox label="是否用于搜索" v-model="param.searching" color="primary" hide-details/>
            <v-flex v-if="param.searching && param.numeric" class="px-2">
              搜索过滤区间:
              <v-layout row wrap v-for="(s,i) in param.segments" :key="i">
                <v-flex xs5>
                  <v-text-field prefix="From:" v-model="s[0]" single-line hide-details/>
                </v-flex>
                <v-spacer/>
                <v-flex xs5>
                  <v-text-field prefix="To:" v-model="s[1]" single-line hide-details/>
                </v-flex>
                <v-flex xs1>
                  <v-btn icon @click="param.segments.splice(i,1)">
                    <i class="el-icon-delete"/>
                  </v-btn>
                </v-flex>
              </v-layout>
              <v-layout row>
                <v-spacer/>
                <v-flex xs1>
                  <v-tooltip left>
                    <v-btn slot="activator" icon @click="param.segments.push([0,0])"><v-icon>add</v-icon></v-btn>
                    <span>点击为数值烈性的参数添加分段，便于搜索过滤</span>
                  </v-tooltip>
                </v-flex>
              </v-layout>
            </v-flex>
          </v-flex>
        </v-card-text>
        <v-card-actions>
          <v-spacer/>
          <v-btn color="blue darken-1" flat @click.native="show=false">取消</v-btn>
          <v-btn color="blue darken-1" flat @click.native="save">保存</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  export default {
    name:"v-spec-param",
    props:{
      group:{ //选中的分组
        type:Object
      }
    },
    data(){
      return {
        headers:[
          {text:"id",value:"id",align:"center",sortable:false},
          {text:"参数名",value:"name",align:"center",sortable:false},
          {text:"是否为数值",value:"numeric",align:"center",sortable:false},
          {text:"单位",value:"unit",align:"center",sortable:false},
          {text:"是否通用",value:"generic",align:"center",sortable:false},
          {text:"是否可搜索",value:"searching",align:"center",sortable:false},
          {text:"操作",align:"center",sortable:false},
        ],
        params:[], //参数
        show:false,
        param:{},
        isEdit:false
      };
    },
    watch:{
      group:{
        deep:true,
        handler(val){
          //监控group的值，如果不为null,则加载数据
          if(val && val.id){
            this.loadData();
          }
        }
      }
    },
    methods:{
      loadData(){
        this.$http.get("/item/spec/params?gid="+this.group.id)
          .then(({data}) =>{
            data.forEach(p =>{
              p.segments = p.segments ? p.segments.split(",").map(s=> s.split("-")) : [];
            })
            this.params = data;
          })
          .catch(()=>{
            this.params = [];
          })
      },
      addParam(i) {
        this.specifications[i].params.push({
          k: "",
          searchable: false,
          global: true,
          numerical:false,
          unit:"",
          options: []
        })
      },
      // 删除指定模板
      deleteParam(i, j) {
        this.specifications[i].params.splice(j, 1);
      },

    }
  }
</script>
<style>

</style>

<!--
<template>
  <v-stepper :value="step">
    <v-stepper-header>
      <v-stepper-step step="1" :complete="step > 1">基本信息</v-stepper-step>
      <v-divider/>
      <v-stepper-step step="2" :complete="step > 2">商品描述</v-stepper-step>
      <v-divider/>
      <v-stepper-step step="3" :complete="step > 3">规格参数</v-stepper-step>
      <v-divider/>
      <v-stepper-step step="4">SKU属性</v-stepper-step>
    </v-stepper-header>
    <v-stepper-items>
      &lt;!&ndash;基本信息的表单&ndash;&gt;
      <v-stepper-content step="1">
        <v-layout justify-center>
          <v-flex xs10>
            <v-form v-model="valid" ref="basic">
              <v-layout row>
                <v-flex xs5>
                  <v-cascader url="/item/category/list" v-model="goods.categories"
                              required show-all-levels label="商品分类"/>
                </v-flex>
                <v-flex offset-xs2 xs5>
                  <v-select label="所属品牌" v-model="goods.brandId" :items="brandOptions" dense autocomplete
                            item-text="name" item-value="id" :rules="[v => !!v || '品牌不能为空']" required/>
                </v-flex>
              </v-layout>

              <v-text-field label="商品标题" v-model="goods.title" :counter="200" required
                            :rules="[v => !!v || '商品标题不能为空']"/>
              <v-text-field label="商品卖点" v-model="goods.subTitle" :counter="200"/>
              <v-text-field label="包装清单" v-model="goods.spuDetail.packingList" :counter="1000" multi-line :rows="3"/>
              <v-text-field label="售后服务" v-model="goods.spuDetail.afterService" :counter="1000" multi-line :rows="3"/>
              <v-layout row>
                <v-flex xs3>
                </v-flex>
                <v-flex>
                </v-flex>
              </v-layout>
            </v-form>
          </v-flex>
        </v-layout>
      </v-stepper-content>
      &lt;!&ndash;商品描述&ndash;&gt;
      <v-stepper-content step="2">
        <quill-editor v-model="goods.spuDetail.description" :options="editorOption"/>
      </v-stepper-content>
      &lt;!&ndash;规格参数&ndash;&gt;
      <v-stepper-content step="3">
        <v-flex offset-xs1 xs10>
          <v-card v-for="(spec, i1) in specifications" :key="i1" class="my-2" v-if="!spec.empty">
            <v-card-title><h4>{{spec.group}}</h4></v-card-title>
            <v-divider/>
            <v-layout column justify-center>
              <v-flex v-for="(param,i2) in spec.params" :key="i2" xs8>
                <v-layout v-if="param.global" row>
                  <v-flex offset-xs2 xs8>
                    <v-text-field v-if="param.options.length <= 0" :suffix="param.unit || ''"
                                  v-model="param.v" :label="param.k"/>
                    <v-select v-else :label="param.k" v-model="param.v" :items="param.options"/>
                  </v-flex>
                </v-layout>
              </v-flex>
            </v-layout>
          </v-card>
        </v-flex>
      </v-stepper-content>
      &lt;!&ndash;SKU属性&ndash;&gt;
      <v-stepper-content step="4">
        <v-layout column>
          <h3>sku属性：</h3>
          <v-divider/>
          <v-flex offset-xs1>
            <div v-for="(t,i) in template" :key="i">
              <h4>{{t.k}}:</h4>
              <div class="px-5" v-if="t.options.length <= 0">
                &lt;!&ndash;默认项&ndash;&gt;
                <v-layout row>
                  <v-flex xs8>
                    <v-text-field :label="`请输入新的${t.k}`" v-model="skuTemplate[i].options[0]"/>
                  </v-flex>
                  <v-spacer/>
                  <v-flex xs1>
                    <v-btn icon small @click="deleteOption(i,0)"><i class="el-icon-delete"></i></v-btn>
                  </v-flex>
                </v-layout>
                &lt;!&ndash;其他项&ndash;&gt;
                <v-layout row v-for="i2 in skuTemplate[i].options.length" :key="i2">
                  <v-flex xs8>
                    <v-text-field :label="`请输入新的${t.k}`" v-model="skuTemplate[i].options[i2]"/>
                  </v-flex>
                  <v-spacer/>
                  <v-flex xs1>
                    <v-btn icon small @click="deleteOption(i,i2)"><i class="el-icon-delete"></i></v-btn>
                  </v-flex>
                </v-layout>
              </div>
              <div class="px-5" v-else>
                <v-container fluid grid-list-xs>
                  <v-layout row wrap>
                    <v-flex v-for="o in t.options" :key="o" xs3>
                      <v-checkbox color="primary" :label="o" v-model="skuTemplate[i].options"
                                  :value="o"/>
                    </v-flex>
                  </v-layout>
                </v-container>
              </div>
            </div>
          </v-flex>
          <v-divider/>
          &lt;!&ndash;sku列表&ndash;&gt;
          <h3>sku列表：</h3>
          <v-flex>
            <v-data-table
              :headers="headers"
              :items="skus"
              hide-actions
              class="elevation-2"
              item-key="indexes"
            >
              &lt;!&ndash; 表单每行样式 &ndash;&gt;
              <template slot="items" slot-scope="props">
                <tr @click="props.expanded = !props.expanded">
                  <td class="text-xs-center" v-for="[k,v] in Object.entries(props.item)" :key="k"
                      v-if="!['id','price','stock','enable','indexes','images'].includes(k)"
                  >{{v}}
                  </td>
                  <td class="text-xs-center">
                    <v-text-field single-line label="0" v-model="props.item.price"
                                  :rules="[val => /^([1-9]\d*)\.?(\d{0,2})$/.test(val) || '价格格式错误']"/>
                  </td>
                  <td class="text-xs-center">
                    <v-text-field single-line label="0" v-model="props.item.stock"
                                  :rules="[val => /^[1-9]\d*$/.test(val) || '库存必须是整数']"/>
                  </td>
                  <td class="text-xs-center">
                    <v-checkbox v-model="props.item.enable"/>
                  </td>
                </tr>
              </template>
              &lt;!&ndash;表单扩展项，当用户点击启用时才展开&ndash;&gt;
              <template v-if="props.item.enable" slot="expand" slot-scope="props" class="px-4">
                <v-card flat class="pb-3">
                  <v-card-title>为商品上传图片：</v-card-title>
                  <v-card-text>
                    <v-upload multiple url="/item/upload" v-model="props.item.images"/>
                  </v-card-text>
                </v-card>
              </template>
              <template slot="no-data">
                <v-alert :value="true" color="warning" icon="warning">
                  请至少选择一个商品属性
                </v-alert>
              </template>
            </v-data-table>
          </v-flex>
        </v-layout>
        <v-layout row>
          <v-flex offset-xs5 xs2>
            <v-btn color="primary" @click="submit">保存商品</v-btn>
          </v-flex>
        </v-layout>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>

<script>
  import 'quill/dist/quill.core.css'
  import 'quill/dist/quill.snow.css'
  import 'quill/dist/quill.bubble.css'

  import {quillEditor} from 'vue-quill-editor'

  export default {
    name: "goods-form",
    components: {
      quillEditor
    },
    props: {
      step: {
        type: Number,
        default: 1,
      },
      isEdit: {
        type: Boolean,
        default: false
      },
      oldGoods: Object
    },
    data() {
      return {
        valid: false,
        editorOption: {// 富文本编辑器配置
          placeholder: '编写商品描述信息，让客户了解你的商品'
        },
        brandOptions: [],// 品牌的待选项
        specifications: [],// 规格参数模板
        template: [],// 特有规格属性模板
        skuTemplate: [],// sku特有属性模板
        skus: [],
        headers: [],// 表头
        goods: {
          categories: null,// 选中的商品分类id数组
          brandId: null,
          title: '',
          subTitle: '',
          spuDetail: {
            packingList: '',
            afterService: '',
            description: '',
            specTemplate: '',// sku属性模板的字符串格式
            specifications:[]
          },
        },
      }
    },
    methods: {
      deleteOption(i, j) {
        this.skuTemplate[i].options.splice(j, 1);
      },
      loadSku(src, dest, i, o) {
        const t = src[i];
        for (let x = 0; x < t.options.length; x++) {
          const obj = {};
          obj[t.k] = t.options[x];
          Object.assign(obj, o);
          if (i === src.length - 1) {
            obj.indexes += x;
            dest.push(obj);
          } else {
            obj.indexes += x + '_';
            this.loadSku(src, dest, i + 1, obj);
          }
        }
      },
      submit() {
        // goods中的商品分类id
        if (!this.goods.categories || this.goods.categories.length < 3) {
          this.$message.error("商品分类填写不正确！")
          return;
        }
        // sku信息
        if (!this.skus || this.skus.length <= 0) {
          this.$message.error("请填写sku信息！")
          return;
        }
        this.goods['cid1'] = this.goods.categories[0].id;
        this.goods['cid2'] = this.goods.categories[1].id;
        this.goods['cid3'] = this.goods.categories[2].id;
        // 转为字符串保存
        const obj = {};
        this.skuTemplate.forEach(t => {
          if(t.options.length > 0){
            obj[t.k] = t.options;
          }
        })
        this.goods.spuDetail.specTemplate = JSON.stringify(obj);

        // 对全局规格参数进行深拷贝
        const specs = [];
        Object.deepCopy(this.specifications, specs);
        specs.forEach(({params}) => {
          params.forEach(p => {
            if (!p.global) {
              p.options = this.skuTemplate[p.k];
            }
          })
        })
        // 处理全局规格参数
        this.goods.spuDetail.specifications = JSON.stringify(specs);

        // 封装sku信息
        this.goods['skus'] = this.skus.filter(s => s.enable).map(s => {
          const {price, stock, enable, indexes, images, ...skuSpecs} = s;
          // 拼接title数据（spu的title加上特有的规格参数）
          let title = this.goods.title;
          Object.values(skuSpecs).forEach(v => title += " " + v);
          return {
            id: s.id || null,
            enable,
            title,
            images: images && images.length > 0 ? images.join(",") : "",
            price : this.$format(price),
            ownSpec: JSON.stringify(skuSpecs),
            indexes,
            stock: {stock}
          }
        })
        // 发起请求
        this.$http({
          url: "/item/goods",
          method: this.isEdit ? "put" : "post",
          data: this.goods
        })
          .then(() => {
            this.$message.success("提交商品成功！");
            this.$emit("closeForm")
          })
          .catch((e) => {
            console.log(e)
            this.$message.error("提交商品失败！");
            this.$emit("closeForm")
          })
      }
    },
    watch: {
      oldGoods: {
        deep: true,
        handler(val) {
          if (val == null || !this.isEdit) {
            return;
          }
          // 实现数据回显
          Object.deepCopy(val, this.goods)
        }
      },
      'goods.categories': {
        deep: true,
        handler(val) {
          // 根据分类加载品牌信息
          this.$http.get("/item/brand/cid/" + val[2].id)
            .then(resp => {
              this.brandOptions = resp.data;
            })
          // 根据分类加载规格参数
          this.$http.get("/item/spec/" + val[2].id)
            .then(resp => {
              this.specifications = resp.data;
              this.template = [];
              this.skuTemplate = [];
              // 过滤出SKU属性
              for (let i = 0; i < this.specifications.length; i++) {
                const params = this.specifications[i].params;
                let x = params.length;
                for (let j = 0; j < params.length; j++) {
                  const p = params[j];
                  if (!p.global) {
                    this.template.push({
                      k:p.k,
                      options:p.options
                    })
                    this.skuTemplate.push({
                      k:p.k,
                      options:[]
                    });
                    x&#45;&#45;;
                  }
                  if (this.isEdit) {
                    p.v = this.goods.spuDetail.specifications[i].params[j].v;
                  }
                }
                if (x === 0) {
                  // 标记当前分组下的全局属性为空
                  this.specifications[i]['empty'] = true;
                }
              }
              // 判断是否修改，需要回显数据
              if (this.isEdit) {
                this.skuTemplate.forEach(t => {
                  t.options = this.goods.spuDetail.specTemplate[t.k]
                });
              }
            })
        }
      },
      skuTemplate: {
        deep: true,
        handler(val) {
          const src = val.filter(v => v.options.length > 0);
          if (src.length <= 0) {
            return;
          }
          this.headers = [];
          this.skus = [];
          this.loadSku(src, this.skus, 0, {
            price: null,
            stock: null,
            indexes: '',
            enable: false
          });
          // 处理回显
          if (this.isEdit) {
            // 查询sku
            this.$http.get("/item/goods/sku/list", {
              params: {
                id: this.goods.id
              }
            }).then(resp => {
              // 处理SKU
              this.skus.forEach(sku => {
                resp.data.forEach(s => {
                  if (sku.indexes === s.indexes) {
                    sku.id = s.id;
                    sku.price = this.$format(s.price);
                    sku.stock = s.stock.stock;
                    sku.images = s.images.split(",");
                    sku.enable = s.enable;
                  }
                })
              })
            })
          }
          // 生成表内容
          this.headers = [
            {text: 'price', align: 'center', sortable: false, value: 'name'},
          ]
          // 生成表头
          this.headers = [];
          if (this.skus.length > 0) {
            Object.keys(this.skus[0]).forEach(text => {
              let width = "80";
              if (text == "indexes") {
                return
              }
              if (text == "price") {
                text = "价格";
                width = "50";
              } else if (text == "stock") {
                text = "库存";
                width = "50";
              } else if (text == "enable") {
                text = "是否启用";
                width = "30";
              }
              this.headers.push({
                text,
                align: 'center',
                sortable: false,
                value: text,
                width
              })
            })
          }
        }
      }
    }
  }
</script>

<style scoped>

</style>
-->
<template>
  <v-stepper v-model="step">
    <v-stepper-header>
      <v-stepper-step :complete="step > 1" step="1">基本信息</v-stepper-step>
      <v-divider/>
      <v-stepper-step :complete="step > 2" step="2">商品描述</v-stepper-step>
      <v-divider/>
      <v-stepper-step :complete="step > 3" step="3">规格参数</v-stepper-step>
      <v-divider/>
      <v-stepper-step step="4">SKU属性</v-stepper-step>
    </v-stepper-header>
    <v-stepper-items>
      <!--1、基本信息-->
      <v-stepper-content step="1">
        <v-flex class="xs10 mx-auto">
          <v-form v-model="valid" ref="basic">
            <v-layout row>
              <v-flex xs5>
                <!--商品分类-->
                <v-cascader
                  url="/item/category/list"
                  required
                  showAllLevels
                  v-model="goods.categories"
                  label="请选择商品分类"/>
              </v-flex>
              <v-spacer/>
              <v-flex xs5>
                <!--品牌-->
                <v-select
                  :items="brandOptions"
                  item-text="name"
                  item-value="id"
                  label="所属品牌"
                  v-model="goods.brandId"
                  required
                  autocomplete
                  clearable
                  dense chips
                  :rules="[v => !!v || '品牌不能为空']"
                >
                  <template slot="selection" slot-scope="data">
                    <v-chip small>{{ data.item.name}}</v-chip>
                  </template>
                </v-select>
              </v-flex>
            </v-layout>
            <v-text-field label="商品标题" v-model="goods.title" :counter="200" required :rules="[v => !!v || '商品标题不能为空']" hide-details/>
            <v-text-field label="商品卖点" v-model="goods.subTitle" :counter="200" hide-details/>
            <v-text-field label="包装清单" v-model="goods.spuDetail.packingList" :counter="1000" multi-line :rows="3" hide-details/>
            <v-text-field label="售后服务" v-model="goods.spuDetail.afterService" :counter="1000" multi-line :rows="3" hide-details/>
          </v-form>
        </v-flex>
      </v-stepper-content>
      <!--2、商品描述-->
      <v-stepper-content step="2">
        <v-editor v-model="goods.spuDetail.description" upload-url="/upload/image"/>
      </v-stepper-content>
      <!--3、规格参数-->
      <v-stepper-content step="3">
        <v-flex class="xs10 mx-auto px-3">
          <!--遍历整个规格参数-->
          <v-card class="my-2">
            <v-container grid-list-md fluid>
              <v-layout wrap row justify-space-between class="px-5">
                <v-flex xs12 sm5 v-for="param in specs" :key="param.name">
                  <v-text-field :label="param.name" v-model="param.v" :suffix="param.unit || ''"
                  />
                </v-flex>
              </v-layout>
            </v-container>
          </v-card>
        </v-flex>
      </v-stepper-content>
      <!--4、SKU属性-->
      <v-stepper-content step="4">
        <v-flex class="mx-auto">
          <!--遍历特有规格参数-->
          <v-card flat v-for="spec in specialSpecs" :key="spec.name">
            <!--特有参数的标题-->
            <div class="subheading">{{spec.name}}:</div>
            <!--特有参数的待选项，需要判断是否有options，如果没有，展示文本框，让用户自己输入-->
            <v-card-text class="px-5">
              <div v-for="i in spec.options.length+1" :key="i" class="layout row px-5">
                <v-text-field :placeholder="'新的' + spec.name + ':'" class="flex xs10" auto-grow
                              v-model="spec.options[i-1]" v-bind:value="i" single-line hide-details/>

                <v-btn @click="spec.options.splice(i-1,1)" v-if="i <= spec.options.length" icon>
                  <i class="el-icon-delete"/>
                </v-btn>
              </div>
            </v-card-text>
          </v-card>
          <v-card class="elevation-0">
            <!--标题-->
            <div class="subheading py-3">SKU列表:</div>
            <v-divider/>
            <!--SKU表格，hide-actions因此分页等工具条-->
            <v-data-table :items="skus" :headers="headers" hide-actions item-key="indexes" class="elevation-0">
              <template slot="items" slot-scope="props">
                <tr @click="props.expanded = !props.expanded">
                  <!--价格和库存展示为文本框-->
                  <td v-for="(v,k) in props.item" :key="k" v-if="['price', 'stock'].includes(k)"
                      class="text-xs-center">
                    <v-text-field single-line v-model="props.item[k]" @click.stop=""/>
                  </td>
                  <!--enable展示为checkbox-->
                  <td class="text-xs-center" v-else-if="k === 'enable'">
                    <v-checkbox v-model="props.item[k]"/>
                  </td>
                  <!--indexes和images不展示，其它展示为普通文本-->
                  <td class="text-xs-center" v-else-if="k !== 'images' && k !== 'indexes'">{{v.v}}</td>
                </tr>
              </template>
              <!--点击表格后展开-->
              <template slot="expand" slot-scope="props">
                <v-card class="elevation-2 flex xs11 mx-auto my-2">
                  <!--图片上传组件-->
                  <v-upload v-model="props.item.images" url="/upload/image"/>
                </v-card>
              </template>
            </v-data-table>
          </v-card>
        </v-flex>
        <!--提交按钮-->
        <v-flex xs3 offset-xs9>
          <v-btn color="info" @click="submit">保存商品信息</v-btn>
        </v-flex>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>

<script>
  export default {
    name: "goods-form",
    props: {
      oldGoods: {
        type: Object
      },
      isEdit: {
        type: Boolean,
        default: false
      },
      step: {
        type: Number,
        default: 1
      }
    },
    data() {
      return {
        valid:false,
        goods: {
          categories: [], // 商品分类信息
          brandId: 0, // 品牌id信息
          title: "", // 标题
          subTitle: "", // 子标题
          spuDetail: {
            packingList: "", // 包装列表
            afterService: "", // 售后服务
            description: "" // 商品描述
          }
        },
        brandOptions: [], // 品牌列表
        specs: [], // 规格参数的模板
        specialSpecs: [] // 特有规格参数模板
      };
    },
    methods: {
      submit() {
        // 表单校验。
        if(!this.$refs.basic.validate){
          this.$message.error("请先完成表单内容！");
        }
        // 先处理goods，用结构表达式接收,除了categories外，都接收到goodsParams中
        const {
          categories: [{ id: cid1 }, { id: cid2 }, { id: cid3 }],
          ...goodsParams
        } = this.goods;
        // 处理规格参数
        const specs = {};
        this.specs.forEach(({ id,v }) => {
          specs[id] = v;
        });
        // 处理特有规格参数模板
        const specTemplate = {};
        this.specialSpecs.forEach(({ id, options }) => {
          specTemplate[id] = options;
        });
        // 处理sku
        const skus = this.skus
          .filter(s => s.enable)
          .map(({ price, stock, enable, images, indexes, ...rest }) => {
            // 标题，在spu的title基础上，拼接特有规格属性值
            const title = goodsParams.title + " " + Object.values(rest).map(v => v.v).join(" ");
            const obj = {};
            Object.values(rest).forEach(v => {
              obj[v.id] = v.v;
            });
            return {
              price: this.$format(price), // 价格需要格式化
              stock,
              indexes,
              enable,
              title, // 基本属性
              images: images ? images.join(",") : '', // 图片
              ownSpec: JSON.stringify(obj) // 特有规格参数
            };
          });
        Object.assign(goodsParams, {
          cid1,
          cid2,
          cid3, // 商品分类
          skus // sku列表
        });
        goodsParams.spuDetail.genericSpec = JSON.stringify(specs);
        goodsParams.spuDetail.specialSpec = JSON.stringify(specTemplate);

        this.$http({
          method: this.isEdit ? "put" : "post",
          url: "/item/goods",
          data: goodsParams
        })
          .then(() => {
            // 成功，关闭窗口
            this.$emit("close");
            // 提示成功
            this.$message.success("保存成功了");
          })
          .catch(() => {
            this.$message.error("保存失败！");
          });
      }
    },
    watch: {
      oldGoods: {
        deep: true,
        handler(val) {
          if (!this.isEdit) {
            Object.assign(this.goods, {
              categories: null, // 商品分类信息
              brandId: 0, // 品牌id信息
              title: "", // 标题
              subTitle: "", // 子标题
              spuDetail: {
                packingList: "", // 包装列表
                afterService: "", // 售后服务
                description: "" // 商品描述
              }
            });
            this.specs = [];
            this.specialSpecs = [];
          } else {
            this.goods = Object.deepCopy(val);

            // 先得到分类名称
            const names = val.cname.split("/");
            // 组织商品分类数据
            this.goods.categories = [
              { id: val.cid1, name: names[0] },
              { id: val.cid2, name: names[1] },
              { id: val.cid3, name: names[2] }
            ];

            // 将skus处理成map
            const skuMap = new Map();
            this.goods.skus.forEach(s => {
              skuMap.set(s.indexes, s);
            });
            this.goods.skus = skuMap;
          }
        }
      },
      "goods.categories": {
        deep: true,
        handler(val) {
          // 判断商品分类是否存在，存在才查询
          if (val && val.length > 0) {
            // 根据分类查询品牌
            this.$http
              .get("/item/brand/cid/" + this.goods.categories[2].id)
              .then(({ data }) => {
                this.brandOptions = data;
              });
            // 根据分类查询规格参数
            this.$http
              .get("/item/spec/params?cid=" + this.goods.categories[2].id)
              .then(({ data }) => {
                let specs = [];
                let template = [];
                if (this.isEdit){
                  specs = JSON.parse(this.goods.spuDetail.genericSpec);
                  template = JSON.parse(this.goods.spuDetail.specialSpec);
                }
                // 对特有规格进行筛选
                const arr1 = [];
                const arr2 = [];
                data.forEach(({id, name,generic, numerical, unit }) => {
                  if(generic){
                    const o = { id, name, numerical, unit};
                    if(this.isEdit){
                      o.v = specs[id];
                    }
                    arr1.push(o)
                  }else{
                    const o = {id, name, options:[]};
                    if(this.isEdit){
                      o.options = template[id];
                    }
                    arr2.push(o)
                  }
                });
                this.specs = arr1;// 通用规格
                this.specialSpecs = arr2;// 特有规格
              });
          }
        }
      }
    },
    computed: {
      skus() {
        // 过滤掉用户没有填写数据的规格参数
        const arr = this.specialSpecs.filter(s => s.options.length > 0);
        // 通过reduce进行累加笛卡尔积
        return arr.reduce(
          (last, spec, index) => {
            const result = [];
            last.forEach(o => {
              spec.options.forEach((option, i) => {
                const obj = JSON.parse(JSON.stringify(o));
                obj[spec.name] = {v:option, id:spec.id};
                obj.indexes = (obj.indexes || '') + '_' +  i
                if (index === arr.length - 1) {
                  obj.indexes = obj.indexes.substring(1);
                  // 如果发现是最后一组，则添加价格、库存等字段
                  Object.assign(obj, {
                    price: 0,
                    stock: 0,
                    enable: false,
                    images: []
                  });
                  if (this.isEdit) {
                    // 如果是编辑，则回填sku信息
                    const sku = this.goods.skus.get(obj.indexes);
                    if (sku != null) {
                      const { price, stock, enable, images } = sku;
                      Object.assign(obj, {
                        price: this.$format(price),
                        stock,
                        enable,
                        images: images ? images.split(",") : [],
                      });
                    }
                  }
                }
                result.push(obj);
              });
            });
            return result;
          },
          [{}]
        );
      },
      headers() {
        if (this.skus.length <= 0) {
          return [];
        }
        const headers = [];
        Object.keys(this.skus[0]).forEach(k => {
          let value = k;
          if (k === "price") {
            // enable，表头要翻译成“价格”
            k = "价格";
          } else if (k === "stock") {
            // enable，表头要翻译成“库存”
            k = "库存";
          } else if (k === "enable") {
            // enable，表头要翻译成“是否启用”
            k = "是否启用";
          } else if (k === "images" || k === 'indexes') {
            // 图片和索引不在表格中展示
            return;
          }
          headers.push({
            text: k,
            align: "center",
            sortable: false,
            value
          });
        });
        return headers;
      }
    }
  };
</script>

<style scoped>
</style>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="农作物信息" prop="cropInfoId" label-width="110px">
        <el-select v-model="queryParams.cropInfoId" placeholder="请选择农作物信息" clearable filterable>
          <el-option v-for="item in corpInfoList" :key="item.id" :label="item.cropName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="记录时间" prop="recordTime">
        <el-date-picker clearable
                        v-model="queryParams.recordTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择记录时间">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:info:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:info:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:info:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:info:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['system:user:import']"
        >导入
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="农作物编号" align="center" prop="cropInfo.cropNum"/>
      <el-table-column label="农作物名称" align="center" prop="cropInfo.cropName"/>
      <el-table-column label="记录时间" align="center" prop="recordTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="温度" align="center" prop="temperature">
        <template v-slot="scope">
          <span>{{ scope.row.temperature }}   °C</span>
        </template>
      </el-table-column>
      <el-table-column label="生长状况" align="center" prop="arowths"/>
      <el-table-column label="湿度" align="center" prop="humidness">
        <template v-slot="scope">
          <span>{{ scope.row.humidness }}   %RH</span>
        </template>
      </el-table-column>
      <el-table-column label="光照" align="center" prop="illumination"/>
      <el-table-column label="农作物图片" align="center" prop="pic">
        <template slot-scope="scope">
          <img :src="scope.row.pic" alt="" style="width: 180px;height: 150px">
        </template>
      </el-table-column>
      <el-table-column label="备注信息" align="center" prop="remark">
        <template v-slot="scope">
          <span v-html="scope.row.remark"></span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:info:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:info:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改农作物种植记录跟踪信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="95px" label-position="left">

<!--        农作物信息的对话框-->
        <el-dialog
          width="30%"
          title="农作物信息"
          :visible.sync="cropInfoIdOpen"
          append-to-body>
          <span style="color: red">改为表格形式选择</span><br><br>
          <el-select v-model="form.cropInfoId" placeholder="请选择农作物信息" :disabled="usable">
            <el-option v-for="item in corpInfoList" :key="item.id" @select="cropInfoIdOpen= false" :label="item.cropName"
                       :value="item.id"></el-option>
          </el-select>
        </el-dialog>
        <el-form-item label="农作物信息" prop="cropInfoId" position="top">
          {{ form.cropInfoId }}

          <!--          农作物信息的对话框-->
          <el-button type="text" @click="cropInfoIdOpen = true ">农作物信息</el-button>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="温度" prop="temperature" style="width: 90%;">
              <el-input v-model="form.temperature" placeholder="请输入温度">
                <template slot="append">°C</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="湿度" prop="humidness">
              <el-input v-model="form.humidness" placeholder="请输入湿度" style="width: 90%">
                <template slot="append">%RH</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生长状况" prop="arowths">
              <el-autocomplete
                popper-class="my-autocomplete"
                v-model="form.arowths"
                :fetch-suggestions="querySearch"
                style="width: 227px"
                placeholder="请输入内容">
                <template slot-scope="{ item }">
                  <div class="name">{{ item.value }}</div>
                  <span class="addr">{{ item.address }}</span>
                </template>
              </el-autocomplete>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="光照" prop="illumination">
              <template>
                <el-radio v-model="form.illumination" label="光照充足"></el-radio>
                <el-radio v-model="form.illumination" label="光照适中"></el-radio>
                <el-radio v-model="form.illumination" label="光照不足"></el-radio>
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="农作物图片" prop="pic">
          <el-upload
            class="avatar-uploader"
            :action="loaderUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <!--1、添加useCustomImageHandler和@image-added="handleImageAdded"-->
          <vue-editor
            id="editor"
            useCustomImageHandler
            @image-added="handleImageAdded"
            v-model="form.remark"
          >
          </vue-editor>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport"/>
            是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                   @click="importTemplate">下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listInfo, getInfo, delInfo, addInfo, updateInfo, CorpInfoList} from "@/api/system/planInfo";
import {VueEditor} from "vue2-editor";
import {getToken} from "@/utils/auth";


export default {
  components: {
    VueEditor
  },
  name: "Info",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 农作物种植记录跟踪信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cropInfoId: null,
        recordTime: null,

      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/plant/records/importData"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      //农作物信息列表
      corpInfoList: [],
      //生长状况输入提示
      restaurants: [],
      //  提交表单 描述的文字域行数
      textRows: 4,
      //提交表单的用户信息是否可用
      usable: false,
      //图片回显路径
      imageUrl: "",
      //  图片上传请求地址
      loaderUrl: "http://localhost/dev-api/file/uploadPic",
    //  农作物信息弹出框
      cropInfoIdOpen : false,



    };
  },
  created() {
    this.getList();
    this.getCorpInfoList();
  },
  methods: {
    /** 查询农作物种植记录跟踪信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        planId: null,
        cropInfoId: null,
        temperature: null,
        arowths: null,
        humidness: null,
        illumination: null,
        pic: null,
        remark: null
      };
      this.imageUrl = null;
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.planId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加农作物种植记录跟踪信息";
      this.usable = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const planId = row.planId || this.ids
      getInfo(planId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改农作物种植记录跟踪信息";
        this.usable = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.planId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const planIds = row.planId || this.ids;
      this.$modal.confirm('是否确认删除农作物种植记录跟踪信息编号为"' + planIds + '"的数据项？').then(function () {
        return delInfo(planIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('plant/records/export', {
        ...this.queryParams
      }, `Plan_${new Date().getTime()}.xlsx`)
      // location = "http://localhost/dev-api/plant/records/export";
    },
    /**
     * 获取农作物信息列表
     */
    getCorpInfoList() {
      CorpInfoList().then(response => {
        this.corpInfoList = response;
      });
    },
    /**
     * 获取生长状态 提示信息列表
     * @param queryString
     * @param cb
     */
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    /** 生长状态 提示列表*/
    loadAll() {
      return [
        {"value": "生长健壮", "address": "植物整体健康，没有病虫害，生长有力"},
        {"value": "生长缓慢", "address": "植物生长速度较慢，可能因为环境条件不佳"},
        {"value": "生长不良", "address": "植物生长状况不佳，可能因为营养不足、病虫害或环境不适"},
        {"value": "果实累累", "address": "植物的果实多而饱满，表明生长状况良好"},
        {"value": "开花稀少", "address": "植物的花朵数量少，可能因为生长条件不佳或花期未到"}
      ]
    },
    /** 图片上传 */
    handleAvatarSuccess(res) {
      //回调请求回显地址
      let url = "http://localhost/dev-api/file/downloadPic?fileName=" + res;
      this.imageUrl = url;
      this.form.pic = url;
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    // 富文本框
    async handleImageAdded(file, Editor, cursorLocation, resetUploader) {
      const formData = new FormData(); //html自带的表单数据类
      formData.append("file", file); //file是文件对象，在服务端接收的字段名也是"file"

      const res = await this.$http.post('upload', formData) //发起请求

      //cursorLocation是光标位置，res.data.url是上传后的url地址
      Editor.insertEmbed(cursorLocation, "image", res.data.url)
      resetUploader() //重置文件上传器
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/user/importTemplate', {}, `user_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },


  },
  mounted() {
    this.restaurants = this.loadAll();
  }
};
</script>
<style>

.name {
  text-overflow: ellipsis;
  overflow: hidden;
}

.addr {
  font-size: 12px;
  color: #00afff;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

</style>

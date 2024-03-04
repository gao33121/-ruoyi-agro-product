<template>
  <div class="app-container">
<!--    <template>-->
<!--      <el-carousel :interval="5000"  arrow="always">-->
<!--        <el-carousel-item v-for="item in avatar" :key="item">-->
<!--         <el-image :src="item.pic"></el-image>-->
<!--        </el-carousel-item>-->
<!--      </el-carousel>-->
<!--    </template>-->

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="农作物名称" prop="cropName">
        <el-input
          v-model="queryParams.cropName"
          placeholder="请输入农作物名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="种植方式" prop="planmethodId">
        <el-select v-model="queryParams.planmethodId" placeholder="请选择入种植方式">
            <el-option :key="p.dictValue" v-for="p in planList" :value="p.dictValue" :label="p.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="灌溉方式" prop="irrigationId">
        <el-select v-model="queryParams.irrigationId" placeholder="请输入灌溉方式">
          <el-option :key="p.dictValue" v-for="p in irrList" :value="p.dictValue" :label="p.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="肥料类型" prop="fertId">
        <el-select v-model="queryParams.fertId" placeholder="请输入肥料类型">
          <el-option :key="p.dictValue" v-for="p in fertList" :value="p.dictValue" :label="p.name"></el-option>
        </el-select>
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
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:info:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
        type="warning"
        plain
        icon="el-icon-download"
        size="mini"
        @click="handleRntry"
        >
          导入
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">

      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="编号  " align="center" prop="cropNum" />
      <el-table-column label="种植基地id" align="center" prop="baseId" />
      <el-table-column label="农作物名称" align="center" prop="cropName" />
      <el-table-column label="登记时间" align="center" prop="registrationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.registrationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="周期" align="center" prop="cycle">
        <template v-slot="pp">
          {{pp.row.cycle}}月
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="baseCharge" />
      <el-table-column label="负责人联系方式" align="center" prop="basePhone" />
      <el-table-column label="种植方式id" align="center" prop="planName" />
      <el-table-column label="灌溉方式id" align="center" prop="irriName" />
      <el-table-column label="肥料类型id" align="center" prop="fertName" />
      <el-table-column label="备注" align="center" prop="remark" >
        <template v-slot="scope">
          <span v-html="scope.row.remark"></span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center"  width="200px" class-name="small-padding fixed-width">
        <template slot-scope="scope" >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:info:remove']"
          >删除</el-button>
          <el-button
          size="mini"
          type="text"
          icon="el-icon-more"
          @click="handleDetails(scope.row)"
          v-hasPermi="['system:info:remove']"
          >详情
          </el-button>
          <el-button
          size="mini"
          type="text"
          icon="el-icon-magic-stick"
          @click="handleRecords(scope.row)"
          >记录农作物信息
          </el-button>
          <el-button
          size="mini"
          type="text"
          icon="el-icon-magic-stick"
          @click="pathListByCrop(scope.row)"
          >查看周期记录信息
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

    <!-- 添加或修改农作物信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="21">
        <el-form-item label="种植id" prop="baseId">
          <el-input v-model="form.baseId" placeholder="请输入种植基地id" />
        </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
        <el-form-item label="作物名称" prop="cropName">
          <el-input v-model="form.cropName" placeholder="请输入农作物名称" />
        </el-form-item>
          </el-col>
          <el-col :span="10">
        <el-form-item label="登记时间" prop="registrationTime">
          <el-date-picker clearable
            v-model="form.registrationTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择登记时间">
          </el-date-picker>
        </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
        <el-form-item label="周期" prop="cycle">
          <el-input v-model="form.cycle" placeholder="请输入周期" style="width: 90%">
          <template slot="append">月份</template>
          </el-input>
        </el-form-item>
          </el-col>
          <el-col :span="12">
        <el-form-item label="负责人" prop="baseCharge">
          <el-input v-model="form.baseCharge" placeholder="请输入负责人" />
        </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="联系方式" prop="basePhone">
          <el-input v-model="form.basePhone" placeholder="请输入负责人联系方式" />
        </el-form-item>
        <el-row>
          <el-col :span="11">
        <el-form-item label="种植方式" prop="planmethodId">
          <el-select v-model="form.planmethodId" placeholder="请选择入种植方式">
            <el-option :key="p.dictValue" v-for="p in planList" :value="p.dictValue" :label="p.name"></el-option>
          </el-select>
        </el-form-item>
          </el-col>
          <el-col :span="12">
          <el-form-item label="灌溉方式" prop="irrigationId">
          <el-select v-model="form.irrigationId" placeholder="请输入灌溉方式">
            <el-option :key="p.dictValue" v-for="p in irrList" :value="p.dictValue" :label="p.name"></el-option>
          </el-select>
        </el-form-item>
          </el-col>
        </el-row>
        <el-row>
        <el-col :span="11">
        <el-form-item label="肥料类型" prop="fertId">
          <el-select v-model="form.fertId" placeholder="请输入肥料类型">
            <el-option  :key="p.dictValue" v-for="p in fertList" :value="p.dictValue" :label="p.name"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
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
    <!-- 详情列表 -->
    <el-dialog :title="title"  :visible.sync="xq" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="21">
            <el-form-item label="种植id"  prop="baseId">
              <el-input disabled v-model="form.baseId" placeholder="请输入种植基地id" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="作物名称" prop="cropName">
              <el-input disabled v-model="form.cropName" placeholder="请输入农作物名称" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="登记时间" prop="registrationTime">
              <el-date-picker
                disabled
                clearable
                              v-model="form.registrationTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择登记时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="周期" prop="cycle">
              <el-input disabled v-model="form.cycle" placeholder="请输入周期" style="width: 90%">
                <template slot="append">月份</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="baseCharge">
              <el-input disabled v-model="form.baseCharge" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="联系方式" prop="basePhone">
          <el-input disabled v-model="form.basePhone" placeholder="请输入负责人联系方式" />
        </el-form-item>
        <el-row>
          <el-col :span="11">
            <el-form-item label="种植方式" prop="planmethodId">
              <el-select disabled v-model="form.planmethodId" placeholder="请选择入种植方式">
                <el-option :key="p.dictValue" v-for="p in planList" :value="p.dictValue" :label="p.name"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="灌溉方式" prop="irrigationId">
              <el-select disabled v-model="form.irrigationId" placeholder="请输入灌溉方式">
                <el-option :key="p.dictValue" v-for="p in irrList" :value="p.dictValue" :label="p.name"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="肥料类型" prop="fertId">
              <el-select disabled v-model="form.fertId" placeholder="请输入肥料类型">
                <el-option  :key="p.dictValue" v-for="p in fertList" :value="p.dictValue" :label="p.name"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <vue-editor
            disabled
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

        <!-- 农作物种植记录跟踪信息对话框 -->
    <el-dialog :title="title" :visible.sync="records" width="750px" append-to-body>
      <el-form ref="form" :model="recordsFrom" :rules="rules" label-width="95px" label-position="left">
        <el-form-item label="记录农作物生长情况" prop="cropInfoId" position="top">
          <el-select v-model="recordsFrom.cropInfoId" placeholder="请选择农作物信息" :disabled="usable">
            <el-option v-for="item in corpInfoList" :key="item.id" :label="item.cropName"
                       :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="温度" prop="temperature" style="width: 90%;">
              <el-input v-model="recordsFrom.temperature" placeholder="请输入温度">
                <template slot="append">°C</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="湿度" prop="humidness">
              <el-input v-model="recordsFrom.humidness" placeholder="请输入湿度" style="width: 90%">
                <template slot="append">%RH</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="生长状况" prop="arowths" >
              <el-autocomplete
                popper-class="my-autocomplete"
                v-model="recordsFrom.arowths"
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
                <el-radio v-model="recordsFrom.illumination" label="光照充足"></el-radio>
                <el-radio v-model="recordsFrom.illumination" label="光照适中"></el-radio>
                <el-radio v-model="recordsFrom.illumination" label="光照不足"></el-radio>
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
          <vue-editor
            id="editor"
            useCustomImageHandler
            @image-added="handleImageAdded"
            v-model="recordsFrom.remark"
          >
          </vue-editor>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="recordedFrom">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
     </el-dialog>
    <!-- 农作物导入对话框 -->
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
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--    查看当前农作物周期记录对话框-->
    <el-dialog :title="planTitle" :visible.sync="dialogTablePhan" width="1000px">
      <el-table :data="planTable" v-loading="loadingPlan" >
        <el-table-column label="记录ID" align="center" prop="planId" width="70"/>
        <el-table-column label="记录时间" align="center" prop="recordTime" width="140">
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
        <el-table-column label="备注信息" align="center" prop="remark" width="180"/>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {listInfo,getrntry,getInfo,addInfo,updateInfo,delInfo,planList,fertList,irrList} from "@/api/system/cropInfo";
import {pathListByCrop} from "@/api/system/planInfo";

// 富文本框
import {getToken} from "../../../utils/auth";
import {VueEditor} from "vue2-editor";
export default {
  name: "Info",
  components: {
    VueEditor
  },
  data() {
    return {
      //  农作物周期记录弹出框
      dialogTablePhan: false,
      // 周期记录弹出框
      planTitle: "",
      //  农作物记录表格数据
      planTable: [],
      loadingPlan: true,
      //图片参数
      avatar:[
        {
          "pic": "https://647bc185.szrtcpa.com/2024/02/12/e8f2c2f363dfb.jpg",
        }, {
          "pic": "https://647bc185.szrtcpa.com/2024/01/25/0cb009abdbc0a.jpg",
        }, {
          "pic": "https://647bc185.szrtcpa.com/2024/02/12/7e62da41ec991.jpg",
        },
      ],
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
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url:process.env.VUE_APP_BASE_API +"/plant/manage/lentInto"
      },
      //记录的列表数据
      recordsFrom:[],
      // 记录对话框
      records:false,
      //肥料类型列表
      fertList:[],
      //灌溉方式列表
      irrList:[],
      //种植方式列表
      planList:[],
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
      // 农作物信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否显示弹出层
      xq:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cropNum: null,
        baseId: null,
        cropName: null,
        registrationTime: null,
        cycle: null,
        baseCharge: null,
        basePhone: null,
        planmethodId: null,
        irrigationId: null,
        fertId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getPlanList();
    this.getIrridList();
    this.getFertList();
  },
  methods: {
    /** 查看当前农作物周期记录按钮*/
    pathListByCrop(crop){
      this.loadingPlan = true;
      pathListByCrop(crop).then(res => {
        this.planTable = res;
        this.planTitle = "编号：" + crop.cropNum + "  的农作物周期记录"
      });
      this.dialogTablePhan = true;
      this.loadingPlan = false;
    },
    recordedFrom(){

    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
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
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 2、添加handleImageAdded函数
    async handleImageAdded(file, Editor, cursorLocation, resetUploader) {
      const formData = new FormData(); //html自带的表单数据类
      formData.append("file", file); //file是文件对象，在服务端接收的字段名也是"file"

      const res=await this.$http.post('upload',formData) //发起请求

      //cursorLocation是光标位置，res.data.url是上传后的url地址
      Editor.insertEmbed(cursorLocation, "image", res.data.url)
      resetUploader() //重置文件上传器
    },
    /** 查看记录方法按钮*/
    handleRecords(){
          this.records=true
    },
    /**导入方法*/
    handleRntry(){
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 详情确定方法*/
    queding(){
      this.xq=false;
    },
    /** 详情方法*/
    handleDetails(row){
      this.reset();
      const id = row.id || this.ids
      getInfo(id).then(response => {
        this.form = response.data;
        this.xq = true;
        this.title = "农作物详情信息";
      });
    },
    /** 查询种植方式列表 */
    getPlanList(){
      planList().then(r=>{
        this.planList=r.data
      })
    },
    /**查询灌溉方式列表*/
    getIrridList(){
      irrList().then(r=>{
        this.irrList=r.data
      })
    },
    /**查询肥料类型列表*/
    getFertList(){
      fertList().then(r=>{
        this.fertList=r.data
      })
    },
    /** 查询农作物信息列表 */
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
        id: null,
        cropNum: null,
        baseId: null,
        cropName: null,
        registrationTime: null,
        cycle: null,
        baseCharge: null,
        basePhone: null,
        planmethodId: null,
        irrigationId: null,
        fertId: null,
        remark: null
      };
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加农作物信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // let planmethodId = row.planmethodId;
      // let irrigationId = row.irrigationId;
      // let fertId = row.fertId;
      this.reset();
      const id = row.id || this.ids
      getInfo(id).then(response => {
        // this.form.planmethodId=planmethodId
        // this.form.irrigationId=irrigationId
        // this.form.fertId=fertId
        this.form = response.data;
        this.open = true;
        this.title = "修改农作物信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除农作物信息编号为"' + ids + '"的数据项？').then(function() {
        return delInfo(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('plant/manage/export', {
        ...this.queryParams
      }, `plan_${new Date().getTime()}.xlsx`)
    },
     //上传图片 样式
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    // 图片的样式
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
    }
  }
};
</script>
<style>
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
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>

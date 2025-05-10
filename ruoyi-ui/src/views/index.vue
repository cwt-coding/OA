<template>
  <div class="oa-home">
    <!-- 顶部统计卡片 -->
    <div class="stat-card-container">
      <div 
        class="stat-card" 
        v-for="(card, index) in statCards" 
        :key="index"
        :class="card.colorClass"
      >
        <div class="card-content">
          <span class="num">{{ card.num }} 个</span>
          <p>{{ card.title }}</p>
        </div>
        <div class="card-icon">
          <i :class="card.iconClass"></i>
        </div>
      </div>
    </div>

    <!-- 主体内容区域 -->
    <div class="main-content">
      <!-- 待办流程 -->
      <div class="todo-section">
        <h3>
          <i class="icon-check"></i>
          待办流程
        </h3>
        <ul v-if="!todoLoading && currentTodoPage.length > 0">
          <li v-for="(item, index) in currentTodoPage" :key="index">
            <div class="item-procDefName">{{ item.procDefName }}</div>
            <div class="item-createTime">{{ item.createTime }}</div>
          </li>
        </ul>
        <div v-else-if="todoLoading" class="loading-container">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else class="no-data-container">
          <i class="icon-empty"></i>
          <span>暂无待办流程</span>
        </div>
      </div>

      <!-- 图片区域 -->
      <div class="image-section">
        <!-- 替换为有效的图片链接 -->
        <img src="https://images.unsplash.com/photo-1574025212411-15c905893f62?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MzR8fCVFOCU4NyVBQSVFNyU4NCVCNiVFOSVBMyU4RSVFNSU4NSU4OXxlbnwwfHwwfHx8MA%3D%3D/800/400" alt="生态环境" />
        <div class="image-desc">
          工作再忙，也不要忘记远处的风景哦
        </div>
      </div>

      <!-- 底部模块 -->
      <div class="bottom-modules">
        <div class="module notice">
          <h3>
            <i class="icon-bell"></i>
            通知
          </h3>
          <ul v-if="!noticeLoading && noticeList.length > 0">
            <!-- 修改：添加点击事件 -->
            <li v-for="(notice, index) in currentNoticePage" :key="index" @click="showDetail(notice)">
              <div class="item-title">{{ notice.noticeTitle }}</div>
              <div class="item-time">{{ notice.createTime }}</div>
            </li>
          </ul>
          <div v-else-if="noticeLoading" class="loading-container">
            <div class="loading-spinner"></div>
            <span>加载中...</span>
          </div>
          <div v-else class="no-data-container">
            <i class="icon-empty"></i>
            <span>暂无通知</span>
          </div>
        </div>

        <div class="module news">
          <h3>
            <i class="icon-file"></i>
            新闻中心
          </h3>
          <ul v-if="!newsLoading && newsList.length > 0">
            <!-- 修改：添加点击事件 -->
            <li v-for="(news, index) in currentNewsPage" :key="index" @click="showDetail(news)">
              <div class="item-title">{{ news.noticeTitle }}</div>
              <div class="item-time">{{ news.createTime }}</div>
            </li>
          </ul>
          <div v-else-if="newsLoading" class="loading-container">
            <div class="loading-spinner"></div>
            <span>加载中...</span>
          </div>
          <div v-else class="no-data-container">
            <i class="icon-empty"></i>
            <span>暂无新闻</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改：添加详细信息模态框 -->
    <div v-if="isDetailVisible" class="detail-modal">
      <div class="detail-content">
        <h2>{{ selectedItem.noticeTitle }}</h2>
        <!-- 使用 v-html 渲染内容 -->
        <p v-html="selectedItem.noticeContent"></p> 
        <p v-html="selectedItem.createTime"></p>
        <button @click="hideDetail" class="close-button">关闭</button>
      </div>
    </div>
  </div>
</template>

<script>
// 导入接口函数
import { todoProcessCount, myProcessCount } from '@/api/flowable/process';
import { getSysUserCount } from '@/api/system/user';
import { getOnlineUserCount } from '@/api/monitor/online';
import { todoListLite } from '@/api/flowable/todo'; 
import { getNotices, getNews } from '@/api/system/notice'; 

export default {
  data() {
    return {
      statCards: [
        { num: 0, title: '我的流程', colorClass: 'purple-card', iconClass: 'icon-check' },
        { num: 0, title: '待办流程', colorClass: 'orange-card', iconClass: 'icon-clock' },
        { num: 0, title: '在线人数', colorClass: 'green-card', iconClass: 'icon-leaf' },
        { num: 0, title: '人员信息', colorClass: 'blue-card', iconClass: 'icon-flag' }
      ],
      todoList: [], 
      noticeList: [], 
      newsList: [], 
      todoLoading: false,
      noticeLoading: false,
      newsLoading: false,
      noticePage: 1, // 通知的当前页码
      newsPage: 1, // 新闻的当前页码
      todoPage: 1, // 待办流程的当前页码
      noticePageSize: 2, // 通知每页展示数量
      newsPageSize: 2, // 新闻每页展示数量
      todoPageSize: 4, // 待办流程每页展示数量
      currentNoticePage: [], // 当前页的通知数据
      currentNewsPage: [], // 当前页的新闻数据
      currentTodoPage: [], // 当前页的待办流程数据
      // 修改：添加显示详细信息的标志和选中的项
      isDetailVisible: false,
      selectedItem: {}
    };
  },
  async created() {
    try {
      await this.getStatData();
      await this.getTodoList();
      await this.getNoticeList();
      await this.getNewsList();
    } catch (error) {
      console.error('数据获取过程中出现错误:', error);
    }
  },
  methods: {
    getStatData() {
      return Promise.all([
        myProcessCount().then(response => {
          console.log('我的流程数量接口返回数据:', response.data);
          this.statCards[0].num = response.data;
        }).catch(error => {
          console.error('获取我的流程数量失败:', error);
        }),
        todoProcessCount().then(response => {
          console.log('待办流程数量接口返回数据:', response.data);
          this.statCards[1].num = response.data;
        }).catch(error => {
          console.error('获取待办流程数量失败:', error);
        }),
        getOnlineUserCount().then(response => {
          console.log('在线人数接口返回数据:', response.data);
          this.statCards[2].num = response.data;
        }).catch(error => {
          console.error('获取在线人数失败:', error);
        }),
        getSysUserCount().then(response => {
          console.log('人员信息数量接口返回数据:', response.data);
          this.statCards[3].num = response.data;
        }).catch(error => {
          console.error('获取人员信息数量失败:', error);
        })
      ]);
    },
    getTodoList() {
      return new Promise((resolve, reject) => {
        this.todoLoading = true;
        todoListLite().then(response => {
            console.log('待办流程列表接口返回数据:', response.data);
            // 确保从返回数据中正确获取 records 数组
            const { records, total } = response.data; 
            if (Array.isArray(records)) {
                this.todoList = records.filter(item => item); 
                // 进行分页处理
                this.currentTodoPage = this.todoList.slice((this.todoPage - 1) * this.todoPageSize, this.todoPage * this.todoPageSize);
            } else {
                console.error('待办流程列表接口返回数据格式错误:', response.data);
                this.todoList = [];
                this.currentTodoPage = [];
            }
            this.todoLoading = false;
            resolve();
        }).catch(error => {
            console.error('获取待办流程列表失败:', error);
            this.todoLoading = false;
            reject(error);
        });
      });
    },
    getNoticeList() {
      return new Promise((resolve, reject) => {
        this.noticeLoading = true;
        const params = {
          page: this.noticePage,
          size: this.noticePageSize
        };
        getNotices(params).then(response => {
          console.log('通知列表接口返回数据:', response.data);
          if (Array.isArray(response.data)) {
            this.noticeList = response.data;
            this.currentNoticePage = this.noticeList.slice((this.noticePage - 1) * this.noticePageSize, this.noticePage * this.noticePageSize);
          } else {
            console.error('通知列表接口返回数据格式错误:', response.data);
            this.noticeList = [];
            this.currentNoticePage = [];
          }
          this.noticeLoading = false;
          resolve();
        }).catch(error => {
          console.error('获取通知列表失败:', error);
          this.noticeLoading = false;
          reject(error);
        });
      });
    },
    getNewsList() {
      return new Promise((resolve, reject) => {
        this.newsLoading = true;
        const params = {
          page: this.newsPage,
          size: this.newsPageSize
        };
        getNews(params).then(response => {
          console.log('新闻列表接口返回数据:', response.data);
          if (Array.isArray(response.data)) {
            this.newsList = response.data;
            this.currentNewsPage = this.newsList.slice((this.newsPage - 1) * this.newsPageSize, this.newsPage * this.newsPageSize);
          } else {
            console.error('新闻列表接口返回数据格式错误:', response.data);
            this.newsList = [];
            this.currentNewsPage = [];
          }
          this.newsLoading = false;
          resolve();
        }).catch(error => {
          console.error('获取新闻列表失败:', error);
          this.newsLoading = false;
          reject(error);
        });
      });
    },
    // 修改：添加显示详细信息的方法
    showDetail(item) {
      this.selectedItem = item;
      this.isDetailVisible = true;
    },
    // 修改：添加隐藏详细信息的方法
    hideDetail() {
      this.isDetailVisible = false;
    }
  }
};
</script>

<style scoped>
.oa-home {
  padding: 20px;
  background: linear-gradient(to bottom, #f5f7fa, #e4ebf1);
  min-height: 100vh;
}

/* 统计卡片样式 */
.stat-card-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  flex: 1;
  padding: 20px;
  border-radius: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.purple-card { background: linear-gradient(135deg, #ffe0f7, #f6b2ff); }
.orange-card { background: linear-gradient(135deg, #fff3e0, #ffe0b2); }
.green-card { background: linear-gradient(135deg, #f0fff0, #c6ffe0); }
.blue-card { background: linear-gradient(135deg, #e6f7ff, #b3e5ff); }

.card-content .num {
  font-size: 24px;
  font-weight: bold;
}

/* 主体内容样式 */
.main-content {
  display: grid;
  grid-template-columns: 3fr 2fr; 
  gap: 20px;
}

.todo-section {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  height: 250px;
  overflow-y: auto; 
}

.todo-section ul li {
  padding: 10px 0;
  border-bottom: 1px solid #eee;
  font-size: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-procDefName {
  text-align: left;
}

.item-createTime {
  text-align: right;
}

.image-section {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  height: 450px; 
}

.image-section img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-desc {
  position: absolute;
  bottom: 20px;
  left: 20px;
  color: white;
  font-size: 20px; 
  font-weight: bold;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

.bottom-modules {
  display: grid;
  grid-template-columns: 1fr 1fr; 
  gap: 20px;
  margin-top: -200px; 
}

.module {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.module ul li {
  list-style: none;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column; /* 垂直排列标题和时间 */
  align-items: flex-start; /* 标题居左 */
  cursor: pointer; /* 修改：添加鼠标指针样式 */
}

.item-title {
  font-size: 16px;         /* 标题字体大小 */
  font-weight: 500;       /* 字体加粗 */
  color: #333;            /* 标题颜色 */
  word-wrap: break-word;   /* 长标题自动换行 */
}

.item-time {
  margin-left: auto; /* 时间右对齐 */
  color: #666;
  font-size: 14px;
}

/* 标题样式 */
h3 {
  margin: 0 0 10px;
  font-size: 20px;
  color: #333;
}

/* 图标样式 */
.icon-check, .icon-bell, .icon-file {
  margin-right: 8px;
  color: #666;
}

/* 新增加载中和无数据样式 */
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 150px;
  color: #666;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 3px solid #f3f3f3;
  border-top-color: #6c757d;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 8px;
}

.no-data-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 150px;
  color: #999;
  font-size: 16px;
}

.no-data-container i {
  font-size: 24px;
  margin-right: 8px;
  color: #e0e0e0;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 新增创建时间样式 */
.create-time {
  color: #666;
  font-size: 12px;
  margin-left: 10px;
}

/* 修改：添加详细信息模态框样式 */
.detail-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.detail-content {
  background-color: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 50%; 
  max-height: 80%;
  overflow-y: auto;
}

/* 让 noticeTitle 居中 */
.detail-content h2 {
  text-align: center;
}

/* 让 createTime 居右 */
.detail-content p:last-of-type {
  text-align: right;
}

/* 让 noticeContent 首行缩进两格 */
.detail-content p:nth-of-type(2) {
  text-indent: 2em;
}

/* 关闭按钮样式 */
.close-button {
  display: block;
  margin-left: auto;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.close-button:hover {
  background-color: #c0392b;
}
</style>    
# SmbTest
Smb  协议，在线播放共享视频，或者下载共享视频

第一步：关闭防火墙

第二步：共享文件夹

第三步：

Step 1. Add the JitPack repository to your build file
	
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
  Step 2. Add the dependency
  
  	dependencies {
	        implementation 'com.github.zhijinjin:SmbTest:v1.0'
	}



代码中
              
     SmbUtil.getInstence()
                .setShareName("用户名")           //共享电脑用户名
                .setSharePwd("密码")              //密码
                .setPC(1)                         //下载进度更新频率（秒）
                .setSvaePath("获取的共享文件")     //下载保存路径
                .setTypes(new String[]{".mp4",".mp3",".html",".rmvb"})      //要获取的文件格式
                .setSearchShareFileLisener(this)       //查询贡献文件监听
                .setLoadShareFileLisener(this);        //下载监听
     
     具体使用参考demo           

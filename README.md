# cloudlibrary
 1.导入流程
 
 1.1最外层的build.gradle中添加：
 ```
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
 1.2app的build.gradle中添加：
 ```
 implementation 'com.github.baiqingsong:cloudlibrary:Tag'
 ```

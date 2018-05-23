# rpg
角色扮演游戏，技术LWJGL

# 操作
1) T和村民对话
2) A攻击怪兽
3) 上下左右方向键控制玩家移动

# 目标：
找到长生不老药Elixir并给PrinceAldric王子救活国王就胜利了。

# 功能：
1) 和村民交流，和Elvira对话时会给玩家加血，和Garth对话会提示你还有哪些物品没收集齐。
2) 攻击被动怪兽，被动怪兽不会攻击你，但是收到攻击时会逃跑。
3) 和主动攻击型怪兽对战，当你靠近主动怪兽时（150像素），主动怪兽会向你移动，当距离够近（50像素）时就会攻击你。
4) 捡物品增加相应技能，增加攻击的物品和增加攻击速度的物品。
5) 玩家死后3秒后复活。

# 运行
环境：jdk1.8, eclipse

下载lwjgl-2.9.1包解压后放在任意路径下，我是放在G:\shadowquest\lwjgl-2.9.1

进入RPG类运行main函数，运行时要配置加载动态链接库，如下
点击run-as->Run Configurations->Arguments->VM arguments
把下面内容填进去，注意自己的路径
-Djava.library.path="G:\shadowquest\lwjgl-2.9.1\native\windows"

然后run as ->java application就可以了。

# 效果
![image](https://github.com/wylc/rgp/blob/master/%E6%95%88%E6%9E%9C%E5%9B%BE%E7%89%87.jpg?raw=true)
![image](https://github.com/wylc/rgp/blob/master/%E6%95%88%E6%9E%9C%E5%9B%BE%E7%89%872.jpg)
![image](https://github.com/wylc/rgp/blob/master/%E6%B8%B8%E6%88%8F%E7%95%8C%E9%9D%A2.jpg)

# SkipList
该项目是一个基于跳表的KV键值对存储数据的项目，里面有三个类，分别为结点类，跳表类和一个主函数
项目是基于跳表的KV键值对存储数据，它的主要功能是：添加数据、删除数据、查找数据、数据落盘和数据加载。
项目采用了java编程语言，跳表的数据结构。
29毫秒可查询100万条语句，比使用MySQL查询快得多；用户直接引入该项目就可使用，结束后调用数据落盘函数把数据存储在文件，下次直接调用数据加载函数就可继续使用上次数据。

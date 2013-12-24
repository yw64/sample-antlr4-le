sample-antlr4-le
================

本工程使用了antlr第三方组件。antlr是一个解析并执行结构化文本的工具，例如sql、hql、xml、代数表达式等都是结构化文本，甚至java、c、c#等程序设计语言也都属于结构化文本的范畴。

本示例使用antlr v4版本。使用antlr4大致分三步：
* 手写一个语法文件（以.g4作为文件名后缀）。
* 通过antlr4提供的工具，从语法文件生成Java、C#等程序文件。
* 手工继承生成的程序，并书写特定的应用逻辑。

本示例演示的场景是“转换逻辑表达式文本”。例如，对于(a) or (b) and (c)字符串，
* 如果(a)无效，则转换为(b) and (c)；
* 如果(b)无效，则转换为(a) or (c)；
* 如果(c)无效，则转换为(a) or (b)；
* 如果全都无效，则转换为null。
* 等等

也就是说，如果一个变量无效，则要删除该变量以及其左边或右边的操作符，至于要删除哪个操作符则要视其语义来决定。

例如上例中，对于变量(b)，其语义是与右边的(c)结合，因此删除(b)时要同时删除与(c)的操作符（and），其最终结果为(a) or (c)。

又如，对于((a) or (b)) and (c)，则当删除(b)时要同时删除与(a)的操作符，其结果为((a)) and (c)。

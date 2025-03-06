# CPABE
Bilateral Access Control based on CP-ABE used JPBC(java)
It surpports any number of attributes.
# Parser_GenerateAccessTree & LSSS_GenerateMatrixFromTree
1. TreeNode.java contains the data structure of an attribute tree, 
it's a binary tree and it's node contains either attribute or bool gate(AND, OR).
2. Parser_GenerateAccessTree.java translates a bool fuction into a tree, some compiling tech used.
3. LSSS_GenerateMatrixFromTree.java will use the tree in step2, and translate to LSSS Matrix
![image](https://github.com/user-attachments/assets/54abd2f0-9581-4bb2-8ee9-49584cb2c916)

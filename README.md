# Vessel_Interpreter ⭐ <br>
A self crafted Tree Walk Interpreter named Vessel inspired from Hollow Knight <//3 <br>
There is no direct references to Hollow Knight except for some keywords here and there 💀 <br>

---

# How to run? 💻 <br>
Clone or download this repo, open the root folder in terminal and compile the Java files:

```bash
cd com/interpreter/vessel
javac *.java
```

after doing so, you can either make your own script in the 'scripts' folder or run the premade scripts, <br>
make sure to put .vessel at the end of the script and run this command.

```bash
java com.interpreter.vessel.vessel scripts/{SCRIPT_NAME}.vessel
```

---

# Vessel Syntax 🎴
Vessel is very much based on python, C and Javascript so there can be many resemblances
<br>

Vessel is a dynamic language thus there is no need to declare variable types, thus a variable is declared as:
```bash
var num = 5;
```
Do not forget to put semicolon at the end
<br>
Printing a statement or variable:
```bash
print "your_name";
print num1 + num2;
```
<br>

If-else statements:
```bash
if(pi == 3.14)
{
  // statement
}
else
{
  // statement
}
```
<br>

And and Or logical operators:
```bash
if(conditionA and conditionB)
{
  // statement
}
if(conditionA or conditionB)
{
  // statement
}
```
<br>

Loops:
Vessel has only a while loop and a for loop and the syntax is almost similar to C
```bash
while(condition)
{
  // statements
}

for(var i = 0; i < n; i = i + 1)
{
  // statements
}
```
Vessel currently does not have the ++ or -- operators so one has to use the ancient methods 
<br>

Functions:
```bash
fun say_gex(a, b, c)
{
  return a + b + c;
}
```
That is it! its amazing how with only these minimal amount of basic functionalities, a programmer can still
make all sorts of different programs and run them in this simple, small and powerful language 🔥🔥

---

# Notes 📍 <br>
This project was built for learning purposes, not for blazing speed or industrial optimization.
But trust — it’s powerful enough to handle most things from basic expressions to complex logic,
if you know how to wield it! 

---

# Credits 🧶 <br>
Made by Anant Kumar Sinha <br>
Crafted by following the book 'Crafting Interpreters' by Robert Nystrom 🤓<br>
Spiritually inspired by Hollow Knight <br>

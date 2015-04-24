#!/usr/bin/python
#python script to extract code metrics from a directory
#proper function requires a list of filenames, I use find command with -f to get only
#filenames. Hard coded for java, python and perl
import sys
import os
import re
#eventually use better variable names for files
fo = open("loc.txt", "a+")
javalines = 0
javafunctions = 0
javaobjects = 0
javacomments = 0
pythonlines = 0
pyfunctions = 0
perlines = 0
bashlines = 0
xmlines = 0
comments = 0
var = set()

foo = open(sys.argv[1], "r")
for line in foo:
   filename, ext = os.path.splitext(line)
   var.add(ext)
   if ".java" in line or ".py" in line or ".pl" in line or ".xml" in line or ".sh" in line:
      fooo = open(line[:-1], "r")
      for lines in fooo:
         lines = lines.strip()
         if ".java" in line:
            if lines:
               if "//" in lines or "/**" in lines:
                  javacomments = javacomments +1
               else:
                  if line[0] != "*":
                     javalines = javalines +1
                  if "{" in lines and "class" not in lines and "public" in lines or "private" in lines:
                     javafunctions = javafunctions +1
                  if " new " in lines:
                     javaobjects = javaobjects +1
         elif ".py" in line:
            if lines:
               if "#" in lines and "\"#" not in line:
                  comments = comments +1
               else:
                  pythonlines = pythonlines +1
                  if "def" in lines:
                     pyfunctions = pyfunctions + 1
         elif ".pl" in line:
            if lines:
               if "#" in lines and "\"#" not in line:
                  comments = comments +1
               else:
                  perlines = perlines +1
         elif ".sh" in line:
            if lines:
               if "#" in lines and "\"#" not in line:
                  comments = comments +1
               else:
                  bashlines = bashlines +1
         elif ".xml" in line:
            if re.findall('<.+>', lines):
               xmlines = xmlines +1
            if "<!--" in lines:
               comments = comments +1
      fooo.close()

fo.write("lines of java code: "+str(javalines)+" functions: "+str(javafunctions)+" objects: "+str(javaobjects)+"comments: "+str(javacomments)+"\n")
fo.write("lines of python code: "+str(pythonlines)+" functions: "+str(pyfunctions)+"\n")
fo.write("lines of perl code: "+str(perlines)+"\n")
fo.write("lines of bash code: "+str(bashlines)+"\n")
fo.write("lines of xml: "+str(xmlines)+"\n")
fo.write("lines of comments of python, perl, java and bash: "+str(comments)+"\n")
fo.write("\n")

filext = open("extensions.txt", "a+")
filext.write("the file extensions are: "+''.join(var))

filext.close()
fo.close()
foo.close()
fooo.close()

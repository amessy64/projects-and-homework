#!/usr/bin/python
#meant to improve on locscript.py written a while ago, the main improvement is that it now can generate a list of files
#in a directory on its own and languages can be added to the program more easily through a class system
#it now works in an interactive loop

#to be improved is the ability to generate further metrics than this simple counting aproach solves
#it also would be cool if we could dynamically add new languages in run time and have that saved for the next time

#some global variables for the program
import os
on = True
mode = "interactive"
userin = ""
path = ""
extensions = set()

#will track metrics for python scripts
class Python:
    def __init__(self):
        self.name = "Python"
        self.code = 0
        self.comments = 0
        self.functions = 0
        self.classes = 0

    def _iscomment(self, s):
        if "#" in s or "\" \" \"" in s:
            return True
        else:
            return False

    def _isfunction(self, s):
        if "def" == s.split(" ")[0]:
            return True
        else:
            return False

    def _isclass(self, s):
        if "class" == s.split(" ")[0]:
            return True
        else:
            return False

    def _checkline(self, s):
        if self._iscomment(s):
            self.comments += 1
            return
        if self._isfunction(s):
            self.functions += 1
        if self._isclass(s):
            self.classes += 1
        self.code += 1

#will track scripts for Java metrics
class Java:
    def __init__(self):
        self.name = "Java"
        self.code = 0
        self.comments = 0
        self.functions = 0
        self.objects = 0
        self.classes = 0

    def _iscomment(self, s):
        if "//" in s or "/**" in s:
            return True
        else:
            return False

    def _isfunction(self, s):
        if "{" in lines and "class" not in lines and "public" in lines or "private" in lines:
            return True
        else:
            return False

    def _isclass(self, s):
        if "class" in s.split(" ")[1]:
            return True
        else:
            return False
    
    def _isobject(self, s):
        if "new" in s:
            return True
        else:
            return False

    def _checkline(self, s):
        if self._iscomment(s):
            self.comments += 1
            return
        if self._isfunction(s):
            self.functions += 1
        if self._isclass(s):
            self.classes += 1
        if self._isobject(s):
            self.objects += 1
        if s[0] != "*":
            self.code += 1

PL = {".java" : Java(), ".py" : Python()}

#recursive function which flattens a directory's files into one list, does not sort out executables and non PL extensions
def separate(dirs, files, path):
    dirs = os.listdir(path)
    for d in dirs:
        d = path+"/"+d
        if os.path.isfile(d):
            files.append(d)
        else:
            newdirs = os.listdir(d)
            separate(newdirs, files, d)
    return files

#the main loop occurs here
path = os.getcwd()
while(on):
    if "interactive" in mode:
        print("Please enter the name of the folder containing the project")
        userin = raw_input()
        if ("quit" in userin or "q" in userin):
            on = False
            continue
        folder = userin
        path = path+"/"+folder
        dirs = []
        files = []
        files = separate(dirs, files, path)
        for f in files:
            filename, ext = os.path.splitext(f)
            if ext in PL.keys():
                file = open(f, "r")
                for lines in file:
                    lines = lines.strip()
                    if lines:
                        PL[ext]._checkline(lines)
        print(PL[".py"].code)
        print(PL[".py"].comments)
        print(PL[".py"].functions)
        print(PL[".py"].classes)

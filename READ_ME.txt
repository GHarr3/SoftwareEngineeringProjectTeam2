--TO-RUN----------------
Open 2 Terminals Windows
Make sure you have Java Installed in Linux/WSL
--1ST-TERMINAL-----------
Go to GAME_MASTER/backend
Run backend.sh, in backend
Should Boot up Springboot
--2ND-TERMINAL-----------
Type in "nc -l -u 127.0.0.1 7500"
--PROGRAM----------------
Go to GAME_MASTER/MAIN_FILES/MAIN
Launch "index.html"
--FUNCTIONALITIES--------
Insert Players:
Pushes all filled in ID and CODENAMES into Database, and prints them on 1st Terminal Window
New Address:
Replaces the default Address 127.0.0.1 with whatever valid address you want, and prints current(new) address on 1st Terminal Window
Clear Players:
Clears all current players on the board from it and deletes them from the Database, also prints the database afterwards
----IMPORTANT------------
If you decide to stop SpringBoot, the program on your first window, make sure to kill it. Run these commands:
ps aux | grep java
To see the PID, should be first line that pops up, first num
EX:
ferna     12345  2.3  1.2 500000 20000 ?  Sl   15:30   0:10 java -jar backend.jar
ferna     12346  0.0  0.0  21584  1060 pts/0 S+   15:45   0:00 grep --color=auto java

PID is 12345

Then run:
kill PID //PID is your, well PID

Then Run program again for further testing, if wanting

--TABLE-----------------
Cooper-H1 -> Cooper Hartman
fernandonavarrete0707-hub -> Fernando Navarrete (Me!)
berteljoey3-create -> Joseph Bertel
GHarr3 -> Gus Harr



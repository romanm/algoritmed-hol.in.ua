cd /home/roman/algoritmed2.com/algoritmed-hol.in.ua
gradlew clean
rm -r bin/
tar cvzf ../algoritmed-hol.in.ua.tar.gz ../algoritmed-hol.in.ua
scp ../algoritmed-hol.in.ua.tar.gz holweb@178.20.157.117:/home/holweb/server
ssh -t holweb@178.20.157.117 "cd ~/server/; bash"


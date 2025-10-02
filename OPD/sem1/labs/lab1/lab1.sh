<<<<<<< HEAD
=======
rm -rf lab0
>>>>>>> 46a3aaf1dc83696a22808d44340096f52fa526ef

mkdir -p lab0

touch lab0/burmy4
mkdir -p lab0/delcatty8
touch lab0/dragonair4
touch lab0/gengar9
mkdir -p lab0/gigalith6
mkdir -p lab0/gliscor9

touch lab0/delcatty8/octillery
touch lab0/delcatty8/vileplume
mkdir -p lab0/delcatty8/ledian
mkdir -p lab0/delcatty8/dratini
mkdir -p lab0/delcatty8/gurdurr
touch lab0/delcatty8/unfezant

touch lab0/gigalith6/cottonee
mkdir -p lab0/gigalith6/lampent
touch lab0/gigalith6/venonat
mkdir -p lab0/gigalith6/blaziken
touch lab0/gigalith6/skiploom

touch lab0/gliscor9/arcanine
touch lab0/gliscor9/baltoy
touch lab0/gliscor9/poochyena
touch lab0/gliscor9/hippopotas


echo "Ходы  Bug Bite Electroweb Snore String Shot" > lab0/burmy4

cat > lab0/delcatty8/octillery <<EOF
Ходы
Aurora Beam Bind Bounce Brine Defense Curl Double-Edge Dive Gunk Shot‡
Icy Wind Mud-Slap Rock Blast‡ Seismic Toss Seed Bomb Signal Beam Sleep
Talk Snore String Shot Swift Water Pulse
EOF

cat > lab0/delcatty8/vileplume <<EOF
Развитие
способности  Effect Spore
EOF

cat > lab0/delcatty8/unfezant <<EOF
satk=7 sdef=6
spd=9
EOF

cat > lab0/dragonair4 <<EOF
Развитие способности  Marvel Scale
EOF

cat > lab0/gengar9 <<EOF
Тип
диеты  Nullivore
EOF

cat > lab0/gigalith6/cottonee <<EOF
Ходы  Covet Endeavor Giga Drain Helping
Hand Knock Off Seed Bomb Sleep Talk Snore Tailwind Worry Seed
EOF

cat > lab0/gigalith6/venonat <<EOF
Способности  Tackle Disable Foresight Supersonic
Confusion Poisonpowder Leech Life Stun Spore Psybeam Sleep Powder
Signal Beam Zen Headbutt Poison Fang Psychic
EOF

cat > lab0/gigalith6/skiploom <<EOF
satk=5 sdef=7
spd=5
EOF

cat > lab0/gliscor9/arcanine <<EOF
Возможности  Overland=16 Surface=6 Jump=5 Power=4
Intelligence=4 Firestarter=0 Tracker=0
EOF

cat > lab0/gliscor9/baltoy <<EOF
Тип диеты
    Terravore
EOF

cat > lab0/gliscor9/poochyena <<EOF
Живёт Forest Grassland
EOF

cat > lab0/gliscor9/hippopotas <<EOF
Возможности
    Overland=4 Surface=4 Underwater=2 Borow6=0 Jump=2 Power=2
    Intelligence=4
EOF



chmod 404 lab0/burmy4
chmod 640 lab0/delcatty8/octillery
chmod 404 lab0/delcatty8/vileplume
chmod 660 lab0/delcatty8/unfezant
chmod 004 lab0/dragonair4
chmod 006 lab0/gengar9
chmod 604 lab0/gigalith6/cottonee
chmod 620 lab0/gigalith6/venonat
chmod 444 lab0/gigalith6/skiploom
chmod 404 lab0/gliscor9/arcanine
chmod 624 lab0/gliscor9/baltoy
chmod 440 lab0/gliscor9/poochyena
chmod 444 lab0/gliscor9/hippopotas

chmod 662 lab0/delcatty8
chmod 315 lab0/delcatty8/ledian
chmod 551 lab0/delcatty8/dratini
chmod 752 lab0/delcatty8/gurdurr
chmod 551 lab0/gigalith6
chmod 674 lab0/gigalith6/lampent
chmod 311 lab0/gigalith6/blaziken
chmod 657 lab0/gliscor9



ln -s delcatty8 lab0/Copy_7

cp lab0/gengar9 lab0/gigalith6/lampent/

cat lab0/burmy4 > lab0/delcatty8/octilleryburmy

ln -s lab0/burmy4 lab0/delcatty8/unfezantburmy

ln lab0/delcatty8 lab0/gigalith6/skiploomburmy

cat lab0/gigalith6/venonat lab0/gliscor9/baltoy > lab0/burmy4_62

cp -r lab0/gliscor9 lab0/gigalith6/blaziken/

<<<<<<< HEAD
wc lab0/gliscor9/arcanine lab0/gliscor9/baltoy > /tmp/arcanine_baltoy.txt 2>&1
ls -lt delcatty8 2>/dev/null
grep -r '' lab0/*y 2>&1 | sort
=======


wc -l lab0/gliscor9/arcanine lab0/gliscor9/baltoy > /tmp/result.txt 2>&1

ls -lt lab0/delcatty8 2>/dev/null

for f in lab0/*y; do
  [ -f "$f" ] && cat "$f" 2>&1 | sort
done

for f in lab0/g*; do
  [ -f "$f" ] && nl "$f" 2>>/tmp/error.log | sort
done

for f in lab0/v*; do
  [ -f "$f" ] && wc -l "$f" 2>>/tmp/error.log
done | sort -nr

cat lab0/delcatty8/vileplume lab0/delcatty8/unfezant lab0/gigalith6/cottonee \
    lab0/gigalith6/venonat lab0/gigalith6/skiploom lab0/gliscor9/arcanine \
    2>/dev/null | grep -v "d$"



chmod 777 lab0/dragonair4 2>/dev/null
rm -f lab0/dragonair4 2>/dev/null

chmod 777 lab0/gigalith6/venonat 2>/dev/null
rm -f lab0/gigalith6/venonat 2>/dev/null

chmod 777 lab0/delcatty8 2>/dev/null
rm -f lab0/delcatty8/unfezantbur* 2>/dev/null

chmod 777 lab0/gigalith6 2>/dev/null
rm -f lab0/gigalith6/skiploombur* 2>/dev/null

chmod 777 lab0/gliscor9 2>/dev/null
rm -rf lab0/gliscor9 2>/dev/null

chmod 777 lab0/delcatty8/ 2>/dev/null
rmdir -f lab0/delcatty8/dratini 2>/dev/null
>>>>>>> 46a3aaf1dc83696a22808d44340096f52fa526ef

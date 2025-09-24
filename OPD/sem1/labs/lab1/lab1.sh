#!/bin/bash

# --- 1. СОЗДАНИЕ ---

# корень
mkdir -p lab0

# файлы и папки первого уровня
touch lab0/burmy4
mkdir -p lab0/delcatty8
touch lab0/dragonair4
touch lab0/gengar9
mkdir -p lab0/gigalith6
mkdir -p lab0/gliscor9

# содержимое delcatty8
touch lab0/delcatty8/octillery
touch lab0/delcatty8/vileplume
mkdir -p lab0/delcatty8/ledian
mkdir -p lab0/delcatty8/dratini
mkdir -p lab0/delcatty8/gurdurr
touch lab0/delcatty8/unfezant

# содержимое gigalith6
touch lab0/gigalith6/cottonee
mkdir -p lab0/gigalith6/lampent
touch lab0/gigalith6/venonat
mkdir -p lab0/gigalith6/blaziken
touch lab0/gigalith6/skiploom

# содержимое gliscor9
touch lab0/gliscor9/arcanine
touch lab0/gliscor9/baltoy
touch lab0/gliscor9/poochyena
touch lab0/gliscor9/hippopotas


# --- 2. КОНТЕНТ ---

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


# --- 3. ПРАВА ---

chmod 755 lab0

chmod 404 lab0/burmy4
chmod 662 lab0/delcatty8
chmod 640 lab0/delcatty8/octillery
chmod 404 lab0/delcatty8/vileplume
chmod 531 lab0/delcatty8/ledian
chmod 551 lab0/delcatty8/dratini
chmod 752 lab0/delcatty8/gurdurr
chmod 660 lab0/delcatty8/unfezant

chmod 004 lab0/dragonair4
chmod 006 lab0/gengar9

chmod 551 lab0/gigalith6
chmod 604 lab0/gigalith6/cottonee
chmod 674 lab0/gigalith6/lampent
chmod 620 lab0/gigalith6/venonat
chmod 611 lab0/gigalith6/blaziken
chmod 444 lab0/gigalith6/skiploom

chmod 657 lab0/gliscor9
chmod 404 lab0/gliscor9/arcanine
chmod 624 lab0/gliscor9/baltoy
chmod 440 lab0/gliscor9/poochyena
chmod 444 lab0/gliscor9/hippopotas

ln -s lab0/delcatty8 lab0/Copy_7
cp lab0/gengar9 lab0/gigalith6/lampent/
cat lab0/burmy4 > lab0/delcatty8/octilleryburmy
ln -s ../burmy4 lab0/delcatty8/unfezantburmy
ln lab0/burmy4 lab0/gigalith6/skiploomburmy
cat lab0/gigalith6/venonat lab0/gliscor9/baltoy > lab0/burmy4_62
cp -r lab0/gliscor9 lab0/gigalith6/blaziken


wc -l lab0/gliscor9/arcanine lab0/gliscor9/baltoy 2>&1 > /tmp/result_wc
ls -lt lab0/delcatty8 2>/dev/null
cat $(find lab0 -type f -name "*y" 2>&1) | sort
cat -n $(find lab0 -type f -name "g*" 2>/dev/null) | sort > /tmp/errors_g
wc -l $(find lab0 -type f -name "v*" 2>/dev/null) 2> /tmp/errors_v | sort -nr
cat lab0/delcatty8/vileplume lab0/delcatty8/unfezant lab0/gigalith6/cottonee lab0/gigalith6/venonat lab0/gigalith6/skiploom lab0/gliscor9/arcanine 2>/dev/null | grep -v "d$"


rm -f lab0/dragonair4
rm -f lab0/gigalith6/venonat
rm -f lab0/delcatty8/unfezantbur*
rm -f lab0/gigalith6/skiploombur*
rm -rf lab0/gliscor9
rm -rf lab0/delcatty8/dratini

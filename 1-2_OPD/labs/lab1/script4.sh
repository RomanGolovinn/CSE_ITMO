wc -l lab0/gliscor9/arcanine lab0/gliscor9/baltoy > /tmp/result.txt 2>&1

ls -lt lab0/delcatty8 2>/dev/null

find lab0 -type f -name '*y' -exec grep -n '^' {} + 2> /tmp/errors.txt | sort

find lab0 -type f -name 'g*' -exec grep -n '^' {} + 2> /tmp/errors.txt | sort

find lab0 -type f -name 'v*' -exec grep -n '^' {} + 2> /tmp/errors.txt | sort -n

cat lab0/delcatty8/vileplume lab0/delcatty8/unfezant lab0/gigalith6/cottonee \
    lab0/gigalith6/venonat lab0/gigalith6/skiploom lab0/gliscor9/arcanine \
    2>/dev/null | grep -v "d$"

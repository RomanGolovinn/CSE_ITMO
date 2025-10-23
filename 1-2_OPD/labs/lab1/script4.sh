wc -l lab0/gliscor9/arcanine lab0/gliscor9/baltoy > /tmp/result.txt 2>&1

ls -lt lab0/delcatty8 2>/dev/null

{
  #cat -n lab0/*y 2>&1
  cat -n lab0/*/*y 2>&1
  #cat -n lab0/*/*/*y 2>&1
} 2>&1 | sort

{
  cat -n lab0/g* 2>&1
  #cat -n lab0/*/g* 2>&1
  cat -n lab0/*/*/g* 2>&1
} 2>&1 | sort

{
  #cat -n lab0/v* 2>&1
  cat -n lab0/*/v* 2>&1
  #cat -n lab0/*/*/v* 2>&1
} 2>&1 | sort

cat lab0/delcatty8/vileplume lab0/delcatty8/unfezant lab0/gigalith6/cottonee \
    lab0/gigalith6/venonat lab0/gigalith6/skiploom lab0/gliscor9/arcanine \
    2>/dev/null | grep -v "d$"

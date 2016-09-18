echo "Setting java opts"

export JAVA_OPTS="-Dcom.sun.management.jmxremote.local.only=false -Djava.rmi.server.hostname='localhost' "
export JAVA_OPTS=$JAVA_OPTS" -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.rmi.port=9090 "
export JAVA_OPTS=$JAVA_OPTS" -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
export JAVA_OPTS=$JAVA_OPTS" -Dcom.sun.management.jmxremote=true"
echo $JAVA_OPTS

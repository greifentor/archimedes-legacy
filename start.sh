LIB=./lib

CP=./target/archimedes-legacy-1.89.1.jar

CP=$CP:$LIB/baccaraacf.jar
CP=$CP:$LIB/commons-lang3-3.1.jar
CP=$CP:$LIB/log4j-1.2.13.jar

FILE=./archimedes-legacy-additions.sh

DEBUG_OPTIONS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n"

ADDITIONAL_OPTIONS=-Darchimedes.maximum.strlen.table.header=50

if [ -f "$FILE" ]; then
    echo Found additions file: $FILE
    source $FILE
fi

java -Xmx384m -Xms384m $DEBUG_OPTIONS $ADDITIONAL_OPTIONS -Darchimedes.img.path=src/main/resources/img -Darchimedes.maximum.strlen.table.header=50 -Darchimedes.Archimedes.debug=true -Darchimedes.gui.ComponentDiagramm.roundCoor=true -Darchimedes.gui.ComponentDiagramm.SmartPositioner.mode=LEFTTOP -Darchimedes.gui.ComponentDiagramm.PAGESPERCOLUMN=4 -Darchimedes.gui.ComponentDiagramm.PAGESPERROW=10 -Darchimedes.gui.FrameArchimedes.import.delay=0 -Darchimedes.gui.FrameArchimedes.import.timeout=90000 -Dcorent.base.StrUtil.suppress.html.note=true -Dcorent.djinn.DefaultEditorDjinnPanel.markup.first.field=true -Dcorent.djinn.ChainKeyAdapter.on.focus.doClick=true -Dcorent.djinn.CreateCycle.vk.f12=VK_F9 -Darchimedes.user.language.resource.path=src/main/resources/conf/ -Darchimedes.Archimedes.properties=src/main/resources/conf/archimedes.properties -cp $CP archimedes.legacy.Archimedes -i ~/archimedes-w.ini

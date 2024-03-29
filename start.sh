#!/bin/bash


LIB=./legacy-core/lib

source set-version.sh

CP=./legacy-core/target/archimedes-legacy-core-$ARCHIMEDES_VERSION-executable.jar

CP=$CP:$LIB/baccaraacf.jar
CP=$CP:$LIB/commons-lang3-3.1.jar
CP=$CP:$LIB/log4j-1.2.13.jar

FILE=../archimedes-legacy-additions/archimedes-legacy-additions.sh

# DEBUG_OPTIONS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n"

ADDITIONAL_OPTIONS="-Darchimedes.maximum.strlen.table.header=50 -Dconf_path=./legacy-core/src/main/resources/conf/ -DCodeFactory.templates.path=legacy-core/src/main/resources/templates"

if [ -f "$FILE" ]; then
    echo Found additions file: $FILE
    source $FILE
fi

java -Xmx4096m -Xms3064m $DEBUG_OPTIONS $ADDITIONAL_OPTIONS -Dlog4j.configurationFile=log4j2.xml -Darchimedes.img.path=legacy-core/src/main/resources/img -Darchimedes.maximum.strlen.table.header=50 -Darchimedes.Archimedes.debug=true -Darchimedes.gui.ComponentDiagramm.roundCoor=true -Darchimedes.gui.ComponentDiagramm.SmartPositioner.mode=LEFTTOP -Darchimedes.gui.ComponentDiagramm.PAGESPERCOLUMN=4 -Darchimedes.gui.ComponentDiagramm.HIT_TOLERANCE=15 -Darchimedes.gui.ComponentDiagramm.PAGESPERROW=10 -Darchimedes.gui.FrameArchimedes.import.delay=0 -Darchimedes.gui.FrameArchimedes.import.timeout=90000 -Dcorent.base.StrUtil.suppress.html.note=true -Dcorent.djinn.DefaultEditorDjinnPanel.markup.first.field=true -Dcorent.djinn.ChainKeyAdapter.on.focus.doClick=true -Dcorent.djinn.CreateCycle.vk.f12=VK_F9 -Darchimedes.user.language.resource.path=legacy-core/src/main/resources/conf/ -Darchimedes.Archimedes.properties=legacy-core/src/main/resources/conf/archimedes.properties -cp $CP archimedes.legacy.Archimedes -i ~/archimedes-w.ini

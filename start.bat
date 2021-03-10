IF NOT EXIST "java-engine-path.bat" GOTO PathSet
    CALL java-engine-path.bat
:PathSet

SET LIB=.\lib

SET CP=.\legacy-core\target\archimedes-legacy-core-1.94.3-executable.jar

SET CP=%CP%;%LIB%\baccaraacf.jar
SET CP=%CP%;%LIB%\commons-lang3-3.1.jar
SET CP=%CP%;%LIB%\log4j-1.2.13.jar

IF "%1"=="NO_DEBUG" GOTO NoDebug
    SET DEBUG_OPTIONS=-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n
:NoDebug

SET ADDITIONAL_OPTIONS=-Darchimedes.maximum.strlen.table.header=50 -Darchimedes.diagram.view.logic.enabled=true -Dconf_path=./legacy-core/src/main/resources/conf/

SET FILE=archimedes-legacy-additions.bat
IF NOT EXIST %FILE% GOTO StartArchimedes
    ECHO Found additions file: %FILE%
    CALL %FILE%
:StartArchimedes

java -Xmx4096m -Xms384m %DEBUG_OPTIONS% %ADDITIONAL_OPTIONS% -Dlog4j.configurationFile=log4j2.xml -Darchimedes.img.path=legacy-core/src/main/resources/img -Darchimedes.Archimedes.debug=true -Darchimedes.gui.ComponentDiagramm.roundCoor=true -Darchimedes.gui.ComponentDiagramm.SmartPositioner.mode=LEFTTOP -Darchimedes.gui.ComponentDiagramm.PAGESPERCOLUMN=4 -Darchimedes.gui.ComponentDiagramm.PAGESPERROW=10 -Darchimedes.gui.FrameArchimedes.import.delay=0 -Darchimedes.gui.FrameArchimedes.import.timeout=90000 -Dcorent.base.StrUtil.suppress.html.note=true -Dcorent.djinn.DefaultEditorDjinnPanel.markup.first.field=true -Dcorent.djinn.ChainKeyAdapter.on.focus.doClick=true -Dcorent.djinn.CreateCycle.vk.f12=VK_F9 -Darchimedes.user.language.resource.path=legacy-core/src/main/resources/conf/ -Darchimedes.Archimedes.properties=%USERPROFILE%/archimedes.properties -cp %CP% archimedes.legacy.Archimedes -i %USERPROFILE%/.archimedes-w.ini

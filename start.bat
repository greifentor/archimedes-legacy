SET LIB=.\lib

SET CP=.\target\archimedes-legacy-1.82.2.jar

SET CP=%CP%;%LIB%\archimedes-core-1.0.jar
SET CP=%CP%;%LIB%\baccara-1.10.1.jar
SET CP=%CP%;%LIB%\baccaraacf.jar
SET CP=%CP%;%LIB%\commons-lang3-3.1.jar
SET CP=%CP%;%LIB%\corent-1.67.1.jar
SET CP=%CP%;%LIB%\corentx-1.46.1.jar
SET CP=%CP%;%LIB%\corentx-taglets-1.23.1.jar
SET CP=%CP%;%LIB%\gengen-1.12.2.jar
SET CP=%CP%;%LIB%\log4j-1.2.13.jar

SET FILE=archimedes-legacy-additions.bat
IF NOT EXISTS %FILE% GOTO StartArchimedes
    ECHO Found additions file: %FILE%
    %FILE%
:StartArchimedes

java -Xmx384m -Xms384m -Darchimedes.Archimedes.debug=true -Darchimedes.gui.ComponentDiagramm.roundCoor=true -Darchimedes.gui.ComponentDiagramm.SmartPositioner.mode=LEFTTOP -Darchimedes.gui.ComponentDiagramm.PAGESPERCOLUMN=4 -Darchimedes.gui.ComponentDiagramm.PAGESPERROW=10 -Darchimedes.gui.FrameArchimedes.import.delay=0 -Darchimedes.gui.FrameArchimedes.import.timeout=90000 -Dcorent.base.StrUtil.suppress.html.note=true -Dcorent.djinn.DefaultEditorDjinnPanel.markup.first.field=true -Dcorent.djinn.ChainKeyAdapter.on.focus.doClick=true -Dcorent.djinn.CreateCycle.vk.f12=VK_F9 -Darchimedes.user.language.resource.path=src/main/resources/conf/ -Darchimedes.Archimedes.properties=src/main/resources/conf/archimedes.properties -cp %CP% archimedes.legacy.Archimedes -i %USERPROFILE%/.archimedes-w.ini

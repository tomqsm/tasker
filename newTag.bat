echo off
set tag=%*
set desc=%*
java -Xmx64m -jar ${project.build.finalName}.${project.packaging} -tag %tag% -desc "%desc%"
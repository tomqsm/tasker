echo off
set desc=%*
java -Xmx64m -jar ${project.build.finalName}.${project.packaging} -useConfig -activity breakCoffe -desc "%desc%"
pause
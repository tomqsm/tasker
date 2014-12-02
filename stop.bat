echo off
set desc=%*
java -Xmx64m -jar ${project.build.finalName}.${project.packaging} -tag stop -desc "%desc%"
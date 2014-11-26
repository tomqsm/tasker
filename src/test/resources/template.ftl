<#ftl>
<#list lines as line>
    minutes: ${line.getDuration().getStandardMinutes()}
</#list>
<#ftl>
<#list lines as line>
    <#assign tag = line.chronicleRecordLine.tag>
    <#assign durationMinutes = line.getDuration().getStandardMinutes()>
    <#assign durationTotalMinutes = durations[tag].getStandardMinutes()>
    <#assign inserted = line.chronicleRecordLine.timestamp?string("HH:mm, EEE, MMM d ")>
    <#assign isSetDurationMinutes = (durationMinutes > 0)>
    <#if isSetDurationMinutes>  
${line.chronicleRecordLine.count})${"\t"}${tag}${"\t"}${durationMinutes} / ${durationTotalMinutes}${"\t"}${inserted}
    <#else>
No duration minutes set.
    </#if>
</#list>
<#list durations?keys as key> 
        ${key} has ${durations[key].getStandardMinutes()} minutes
</#list> 
<#ftl>
<#list lines as line>
    <#assign tag = line.tag>
    <#assign durationHours = line.getDuration().toPeriod().getHours()>
    <#assign durationMinutes = line.getDuration().toPeriod().getMinutes()>
    <#assign durationTotalMinutes = line.getTotalDuration().getStandardMinutes()>
    <#assign inserted = line.timestamp?string("HH:mm:ss, EEE, MMM d ")>
    <#assign isSetDurationMinutes = (durationMinutes >= 0)>
    <#if isSetDurationMinutes>  
${line.count})${"\t"}${tag}${"\t"}${durationHours} H:${durationMinutes} min / ${durationTotalMinutes}${"\t"}${inserted}
    <#else>
No duration minutes set.
    </#if>
</#list> 
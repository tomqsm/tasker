<#ftl>
<#setting locale="pl">
<#if namingRecords?has_content>
    <#list namingRecords as nr>
            XX ${nr.getId()}
    </#list>
        </#if>
<#list lines as line>
    <#assign tag = line.tag>
    <#assign durationHours = line.getDuration().toPeriod().getHours()>
    <#assign durationTotalHours = line.getTotalDuration().toPeriod().getHours()>
    <#assign durationMinutes = line.getDuration().toPeriod().getMinutes()>
    <#assign durationTotalMinutes = line.getTotalDuration().toPeriod().getMinutes()>
    <#assign inserted = line.timestamp?string("HH:mm, EEE, MMM d ")>
    <#assign isSetDurationMinutes = (durationMinutes >= 0)>
    <#if isSetDurationMinutes>  
${line.count})${"\t"}[${line.getId()?string.number}]${tag}${"\t"}${durationHours}:${durationMinutes} min / ${durationTotalHours}:${durationTotalMinutes}${"\t"}${inserted}
<#if line.description?has_content>
${"\t"}${line.description}
</#if>
    <#else>
No duration minutes set.
    </#if>
</#list> 
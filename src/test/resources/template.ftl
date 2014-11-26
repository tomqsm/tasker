<#ftl>
<#list lines as line>
<#assign durationMinutes = line.getDuration().getStandardMinutes()>
<#assign durationTotalMinutes = line.getTotalDuration().getStandardMinutes()>
<#assign inserted = line.chronicleRecordLine.timestamp?string("HH:mm, EEE, MMM d ")>
<#assign isSetDurationMinutes = (durationMinutes > 0)>
<#if isSetDurationMinutes>  
${line.chronicleRecordLine.count})${"\t"}${line.chronicleRecordLine.tag}${"\t"}${durationMinutes} / ${durationTotalMinutes}${"\t"}${inserted}
<#else>
No duration minutes set.
</#if>
</#list>
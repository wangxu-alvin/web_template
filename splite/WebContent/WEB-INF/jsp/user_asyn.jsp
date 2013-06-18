<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
		<link rel="stylesheet" type="text/css" href="script/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="script/easyui/themes/icon.css">
		<script type="text/javascript" src="script/easyui/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="script/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="script/easyui/locale/easyui-lang-${pageContext.response.locale}.js"></script>
	</head>
	<body>
		<div style="margin-top: 30px">
		<div id="toolbar">
			<div>  
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUser()"><spring:message code="user.add" /></a>  
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()"><spring:message code="user.edit" /></a>  
		        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyUser()"><spring:message code="user.remove" /></a>
	        </div>
	        <div>  
		        <input class="easyui-searchbox" data-options="prompt:'<spring:message code="input.value" />',menu:'#mm',searcher:doSearch" style="width:180px"></input>  
			    <div id="mm" style="width:120px">  
			        <div data-options="name:'name'"><spring:message code="user.name" /></div>  
			        <div data-options="name:'email'"><spring:message code="user.email" /></div>  
			    </div>  
	        </div>
	    </div> 
		<table id="dg" class="easyui-datagrid" 
			style="width:700px;height:400px;"  
            title="<spring:message code="pagination.sample" />" 
            rownumbers="true" pagination="true" url="userAsyn.jspa"
            method="post" fitColumns="true" idField="id", striped="true", toolbar="#toolbar", singleSelect="true">  
	        <thead>  
	            <tr>  
	                <th data-options="field:'name'" width="20%"><spring:message code="user.name" /></th>  
	                <th data-options="field:'email'" width="40%"><spring:message code="user.email" /></th>  
	                <th data-options="field:'lastModified',formatter:function(value,row,index){var d = new Date(value); return d.toLocaleString();}" width="80" align="left"><spring:message code="last.modified" /></th>  
	            </tr>  
	        </thead>
	    </table>
	    </div>
	    <div id="dlg" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px"  
                closed="true" buttons="#dlg-buttons">  
            <div class="ftitle"><spring:message code="user.info" /></div>  
            <form id="fm" method="post" novalidate>  
                <div class="fitem">  
                    <label><spring:message code="user.name" />:</label>  
                    <input id="name" name="name" class="easyui-validatebox" required="true">  
                </div>  
                <div class="fitem">  
                    <label><spring:message code="user.password" />:</label>  
                    <input id="password" name="password" class="easyui-validatebox" required="true">  
                </div>  
                <div class="fitem">  
                    <label><spring:message code="user.email" />:</label>  
                    <input id="email" name=email class="easyui-validatebox" validType="email">  
                </div>  
	            <input type="hidden" id="lastModified" name="lastModified" >
	            <input type="hidden" id="id" name="id" >
            </form>  
        </div>  
        <div id="dlg-buttons">  
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()"><spring:message code="save" /></a>  
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')"><spring:message code="cancel" /></a>  
        </div>
        <script type="text/javascript"> 
            var url;  
            function newUser(){  
                $('#dlg').dialog('open').dialog('setTitle','<spring:message code="user.add" />');  
                $('#fm').form('clear');
                $('#id').attr("name", "none");
                $('#lastModified').attr("name", "none");
                url = 'addUserAsyn.jspa';
            }
            function editUser(){
                var row = $('#dg').datagrid('getSelected');
                if (row){
                	$('#id').attr("name", "id");
                    $('#lastModified').attr("name", "lastModified");
                    $('#dlg').dialog('open').dialog('setTitle','<spring:message code="user.edit" />');  
                    $('#fm').form('clear');
                    $('#fm').form('load',row); 
                    url = 'updateUserAsyn.jspa';  
                } else {
                	$.messager.show({  
                        title: '<spring:message code="remind" />',  
                        msg: '<spring:message code="select.row" />',
                        showType:'fade',  
                        style:{  
                            right:'',  
                            bottom:''  
                        },
                        timeout:5000
                    }); 
                }
            }  
            function saveUser(){  
                $('#fm').form('submit',{  
                    url: url,  
                    onSubmit: function(){  
                        return $(this).form('validate');  
                    },  
                    success: function(result){  
                        var result = eval('('+result+')');  
                        if (result.message){  
                            $.messager.show({  
                                title: 'Error',  
                                msg: result.message,
                                showType:'fade',  
                                style:{  
                                    right:'',  
                                    bottom:''  
                                },
                                timeout:15000
                            });  
                        } else {
                            $('#dlg').dialog('close');      // close the dialog  
                            $('#dg').datagrid('reload');    // reload the user data  
                        }  
                    }  
                });  
            }  
            function destroyUser(){  
                var row = $('#dg').datagrid('getSelected');  
                if (row){  
                    $.messager.confirm('<spring:message code="user.remove"/>','<spring:message code="delete.confirmation"/> ' + row.name +' ? ', function(r){  
                        if (r){  
                            $.post('deleteUserAsyn.jspa',{id:row.id}, function(result){  
                                if (result.success){  
                                    $('#dg').datagrid('reload');    // reload the user data  
                                    $('#dg').datagrid('clearSelections');
                                } else {  
                                    $.messager.show({   // show error message  
                                        title: 'Error',  
                                        msg: result.message  
                                    });  
                                }  
                            });  
                        }  
                    });  
                } else {
                	$.messager.show({  
                        title: '<spring:message code="remind" />',  
                        msg: '<spring:message code="select.row" />',
                        showType:'fade',  
                        style:{  
                            right:'',  
                            bottom:''  
                        },
                        timeout:5000
                    }); 
                }
            }
            function doSearch(value, name){
                $('#dg').datagrid('load', {  
                    type: name,  
                    condition: value  
                });  
            }  
        </script>  
		<style type="text/css">  
            #fm{  
                margin:0;  
                padding:10px 30px;  
            }  
            .ftitle{  
                font-size:14px;  
                font-weight:bold;  
                padding:5px 0;  
                margin-bottom:10px;  
                border-bottom:1px solid #ccc;  
            }  
            .fitem{  
                margin-bottom:5px;  
            }  
            .fitem label{  
                display:inline-block;  
                width:80px;  
            }  
        </style> 
	</body>
</html>
$(function() {
	
	$(".btn-import").click(function(event) {
		var $subjectType=$('#subjectType').val();
		$.mydialog({
		  id:'import',
		  width:600,
		  height:415,
		  zIndex:11000,
		  content: 'url:importForm?subjectType='+$subjectType
		});
	});

    /**
	 * 自动生成开考科目
     */
    $(".btn-autoExamPlan").click(function(event) {
        $.mydialog({
            id:'import',
            width:600,
            height:415,
            zIndex:11000,
            content: 'url:autoExamPlan'
        });
    });
	
	$(".btn-export").click(function(event) {
		var url = ctx + "/exam/new/plan/export?" + $("#listForm").serialize();
		window.open(url);
	});
	
});


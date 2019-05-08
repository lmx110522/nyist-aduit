Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
}; //删除某个数组中制定的一个值

var arr = ['person1', 'person2', 'person3', 'person4', 'person5', 'person6', 'person7', 'person8'] //这里存放所有老师的 
var arr_t = []

$('.model').chosen({
    width: '100%', //设置select宽度
    max_selected_options: 10, //设置每一项的最大选值
    search_contains: true, //开启模糊搜索
    no_results_text: "未找到此选项!", //搜索框内没找到返回的数据
});


$('.model').on('change', function (e, params) {   //当选项改变的时候
    // alert('哈哈');
    if (params.selected !== undefined) {  //如果选人值不为空 进入if   params.selected的值就是
         console.log(params.selected.split('_')[1])    // params.selected和params.deselected分别是每次添加或删除的值
        arr_t.push(params.selected.split('_')[1])   //追加到arr_t数组中  与后面的数组remove方法相结合 使得arr_t数组中存放的就是所有模块被选中的总值
    }
    if (params.deselected !== undefined) { //如果删除的人存在的话 进入if
        console.log(params.deselected.split('_')[1])  //测试
        arr_t.remove(params.deselected.split('_')[1])   //就把这个删除的人从arr_t数组中剔除
        $(".model").find("option[class~=" + params.deselected.split('_')[1] + "]").removeClass("myselected") //如果用户删除或取消某个选人 就让其重新返回备选列表中
    }
    // console.log(arr_t)
    for (var i = 0; i < arr_t.length; i++) {
        $(".model").find("option[class=" + arr_t[i] + "]").addClass("myselected") //把选中的都加上控制类名删除

    }

    $(".model").trigger("chosen:updated"); //更新下拉框
});



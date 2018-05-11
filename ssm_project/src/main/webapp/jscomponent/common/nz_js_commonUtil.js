
	/**
	 * 格式化sum出来的钱数
	 */
	fmtMoney = function(num,centsTag) {
		if (!num) return "-";
			num = num.toString().replace(/$|\,/g, '');
		if (isNaN(num) || num==0)
			return "-";
		
		var centsFlag = getcentsFlag(centsTag);
		
		sign = (num == (num = Math.abs(num)));//正负标示 :true 是正数 false是负数 
		num = Math.floor(num * centsFlag + 0.50000000001);
		cents = num % centsFlag;
		var centsLength = cents.toString().length;
		num = Math.floor(num / centsFlag).toString();
		if (centsLength < centsTag){
			cents = getcentsFlag(centsTag - centsLength).toString().substring(1) + cents;
		}
		for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
			num = num.substring(0, num.length - (4 * i + 3)) + ',' +
			num.substring(num.length - (4 * i + 3));
		return "" + (((sign) ? '' : '-') + '' + num + '.' + cents);
	}
	
	function getcentsFlag(flag){
		if(isNaN(flag)){
			flag = 2;
		}
		var tag = '1';
		for(var j = 0 ; j < flag ; j++){
			tag += '0';
		}
		return parseInt(tag);
	}
	
	
	
	//合并单元格
	function merge(mergeCellsColumenIndexs){
			$(".l-grid-row").each(function(trIndex,tr){
				$(tr).find('td').each(function(tdIndex,td){
						var rowspanCount = 1;
						var thisValue = $(td).find('div').html();
						var columnIndex = $(td).attr('id').split('|')[3];
						if(in_arrays(columnIndex,mergeCellsColumenIndexs)){	
						    rowspanCount = mergeCells(tr,columnIndex,thisValue,rowspanCount);
						    $(td).attr('rowspan',rowspanCount);
					    }
				})
			})
	}
	function mergeCells(tr,tdIndex,thisValue,rowspanCount){
		
		try{
			var nextMatchTd = $(tr).next().find('[id$='+tdIndex+']');
			var nextValue = $(nextMatchTd).find('div').html();
			if(nextValue==""){
				rowspanCount = rowspanCount + 1;
				nextMatchTd.remove();
				return mergeCells($(tr).next(),tdIndex,thisValue,rowspanCount);
				
			}else{
				return rowspanCount;
			}
		}catch(e){
			alert(e);
		}
	}
	function in_arrays(columenIndex,columenIndexs){
		var isInArrays = false;
		for(var i=0;i<columenIndexs.length;i++){
			if(columenIndex==columenIndexs[i]){
				isInArrays = true;
				break;
			}
		}
		return isInArrays;
	}
	
	function replaceAllSign( str ){
		str = str.replace(/&lt;/g,'<');
		str = str.replace(/&gt;/g,'>');
		str = str.replace(/&quot;/g,'\"');
		str = str.replace(/&apos;/g,'\'');
		str = str.replace(/&#39;/g,'\'');
		return str ;
	}
	
	/**
	 * 通用文件类型图标
	 * @param type
	 * @returns {String}
	 */
	function trans2IconClass(type) {
		var typeIndexof=type.indexOf(".");
		var fileType= typeIndexof>=0?type.substring(typeIndexof+1):type;//截取后缀
		return "style='background:url(images/icon/nicon/"+fileType.toLowerCase()+".gif) left 1px no-repeat;'";
	}
	
	/**
	 * 初始化界面，让给界面上所有满足规则的加上冒泡提示。默认宽度200
	 */
	function initTip(){
		$("[tipContent]").unbind("mouseover",null);
		$("[tipContent]").unbind("mouseout",null); 
		var isclose=true;
		var timeout=null;
		$("[tipContent]").mouseover(function(){
			if(timeout!=null){clearTimeout(timeout);timeout=null;}
			$(".sharp").remove();
			var tipContent=$(this).attr("tipContent");
			var tipWidth=$(this).attr("tipWidth")==null?"200px":$(this).attr("tipWidth");
			var tipWidthNumber=0;
			if(tipContent==null||$.trim(tipContent)==""){return false;}
			var divhtml  =  '<div class="sharp color1" style="width:'+tipWidth+'">'+
							'	 <span class="s_itop">'+
							'		<i class="i1"></i>'+
							'		<i class="i2"></i>'+
							'		<i class="i3"></i>'+
							'		<i class="i4"></i>'+
							'		<i class="i5"></i>'+
							'		<i class="i6"></i>'+
							'	 </span>'+
							'        <b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>'+ 
							'        <div class="tipContentInfo">  '+tipContent+
							'        </div>'+
							'        <b class="b5"></b><b class="b6"></b><b class="b7"></b><b class="b8"></b>'+
							'	 <span class="s_ibttom">'+
							'		<i class="i6"></i>'+
							'		<i class="i5"></i>'+
							'		<i class="i4"></i>'+
							'		<i class="i3"></i>'+
							'		<i class="i2"></i>'+
							'		<i class="i1"></i>'+
							'	 </span>'+
							'  </div> 	';
			var E=$(this);
			$('body').append(divhtml);
			var sharpDiv= $(".sharp");
			if(E.offset().top - sharpDiv.height() < 0){
				sharpDiv.css("top",E.offset().top + E.height() + 5);
				$(".s_ibttom").remove();
			}else{
				sharpDiv.css("top",E.offset().top - sharpDiv.height() - 5);
				$(".s_itop").remove();
			}
			if(E.offset().left + sharpDiv.width() > getTotalWidth()){
				var sharpLeft = E.offset().left - sharpDiv.width() + 30;//计算浮动提示框的上面边距离位置
				sharpDiv.css("left",sharpLeft);
				tipWidthNumber = E.offset().left - sharpLeft + (E.width() * 0.5);//计算提示小三角形的位置
			}else{
				var sharpLeft = E.offset().left - 30;//计算浮动提示框的左边距离位置
				sharpDiv.css("left",sharpLeft);
				tipWidthNumber=E.offset().left - sharpLeft + (E.width() * 0.5);//计算提示小三角形的位置
			}
			sharpDiv.show();
			//计算I的位置
			$("i").each(function(){
				var number= parseInt($(this).attr("class").substring(1));
			    $(this).css("margin-left",(tipWidthNumber-number)+"px");
			});
			 sharpDiv.mouseover(function(){
				if(timeout!=null){
					clearTimeout(timeout);
					timeout=null;
				}
				$(".sharp").show();
			}).mouseout(function(){ 
				$(".sharp").hide();
			})
		}).mouseout(function(){
			if(timeout==null){
//				$(".sharp").hide();
				timeout=setTimeout(function(){$(".sharp").hide();},200);
			}
		});
	}
	//获取窗口的宽度
	function getTotalWidth(){
        if($.browser.msie){
            return document.compatMode == "CSS1Compat"? document.documentElement.clientWidth :
                     document.body.clientWidth;
        }else{
            return self.innerWidth;
        }
	}
	

	


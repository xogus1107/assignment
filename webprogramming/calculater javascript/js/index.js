;(function($) {

  function Calculator() {
    this.$displayElement = $('.display-text');
    this.$memory = 0;
    this.emptyDisplay = function(val) {
      return val === '0' || val === 'ERROR';
    }
    this.displayLastCharOperator = function(str) {
      arr = str.split('');
      return /(\+|\-\|×|\÷)/.test( arr[arr.length - 1] );
    }
    this.evaluateExpression = function(exp) {
      var result;
      try {        
        var outResult, result, inVal = exp;
            function stringFormulaToArray(stringFormula) {
              var tempArr = [],
                tempVar = '',
                leftCount = 0,
                rightCount = 0,
                tempFormul;
              tempFormul = stringFormula[stringFormula.length - 1];
              for (var i = 0; i < stringFormula.length; i++) {
                if (stringFormula[i] === '(') {
                  leftCount++;
                }
                if (stringFormula[i] === ')') {
                  rightCount++;
                }
              }
              if ((leftCount !== rightCount) || ((isNaN(tempFormul)) && (tempFormul !== ')'))) {
                return 'Error';
              } else {

                for (var i = 0; i < stringFormula.length; i++) {
                  if ((isNaN(stringFormula[i])) && (stringFormula[i] !== '.')) {
                    if (tempVar === '') {
                      tempArr.push(stringFormula[i]);
                      tempVar = '';
                    } else {
                      tempArr.push(tempVar);
                      tempVar = '';
                      tempArr.push(stringFormula[i]);
                    }
                  } else if (i !== stringFormula.length - 1) {
                    tempVar += stringFormula[i];
                  } else {
                    tempVar += stringFormula[i];
                    tempArr.push(tempVar);
                    tempVar = '';
                  }
                }
              }
              return tempArr;
            }

            function symbolPriority(inSymbol) {
              switch (inSymbol) {
                case ')':
                  return 0;
                  break;
                case '(':
                  return 1;
                  break;
                case '+':
                  return 2;
                  break;
                case '-':
                  return 2;
                  break;
                case '×':
                  return 3;
                  break;
                case '÷':
                  return 3;
                  break;
                default:
                  return 'Error!'
                  break;
              }
            }

            function toReversePolishNotation(inVar) {
              var stack = [],
                rpn = [],
                tempVal;
              tempVal = stringFormulaToArray(inVar);
              if (tempVal !== 'Error') {
                for (var i = 0; i < tempVal.length; i++) {
                  if (!isNaN(parseFloat(tempVal[i]))) {
                    if (i === tempVal.length - 1) {
                      rpn.push(tempVal[i]);
                      while(stack.length>0){
                      rpn.push(stack.pop());
                      }
                    } else {
                      rpn.push(tempVal[i]);                      
                    }
                  } else if (symbolPriority(tempVal[i]) === 1) {
                    stack.push(tempVal[i]);
                  } else if (symbolPriority(tempVal[i]) === 2) {
                    if ((stack.length === 0) || (symbolPriority(stack[stack.length - 1]) === 1)) {
                      stack.push(tempVal[i]);
                    } else if ((symbolPriority(stack[stack.length - 1]) === 2) || (symbolPriority(stack[stack.length - 1]) === 3)) {
                      rpn.push(stack.pop());
                      if ((symbolPriority(stack[stack.length - 1]) === 2) || (symbolPriority(stack[stack.length - 1]) === 3)) {
                        rpn.push(stack.pop());
                        stack.push(tempVal[i]);
                      } else {
                        stack.push(tempVal[i]);
                      }
                    } else {
                        stack.push(tempVal[i]);
                      }
                  } else if (symbolPriority(tempVal[i]) === 3) {
                    if ((stack.length === 0) || (symbolPriority(stack[stack.length - 1]) === 1)) {
                      stack.push(tempVal[i]);
                    } else if (symbolPriority(stack[stack.length - 1]) === 3) {
                      rpn.push(stack.pop());
                      if (symbolPriority(stack[stack.length - 1]) === 3) {
                        rpn.push(stack.pop());
                        stack.push(tempVal[i]);
                      } else {
                        stack.push(tempVal[i]);
                      } 
                    } else {
                        stack.push(tempVal[i]);
                      }
                  } else if (symbolPriority(tempVal[i]) === 0) {
                    for (var j = stack.length - 1; j > 0; j--) {
                      if (symbolPriority(stack[j]) !== 1) {
                        rpn.push(stack.pop());
                      } else {
                        stack.pop();
                        break;
                      }
                    }
                  }
                }
                return rpn;
              } 
			  else if(inVar[inVar.length-1]==')'){
				return 'Error - incorrect formula';
			  }
			  else {
                return 'Error - incorrect formula';
              }
            }

            function solveReversePolishNotation(formulaInRPN) {
              var answerOfSolve = 0,
                arrayOfTempAnswers = [],
                tempVariable, glass, tempFormula = [],
                tempAnswer;
              for (var i = 0; i < formulaInRPN.length; i++) {
                glass = formulaInRPN[i];
                tempFormula[i] = glass;
              }
              var i = -1;
              while (tempFormula.length > 2) {
                i++;
                if ((!isNaN(tempFormula[i])) && (!isNaN(tempFormula[i + 1])) && (isNaN(tempFormula[i + 2]))) {
                  if (tempFormula[i + 2] === '+') {
                    tempAnswer = (parseFloat(tempFormula[i]) + parseFloat(tempFormula[i + 1]));
                    tempFormula[i] = tempAnswer;
                    tempFormula.splice(i + 1, 2);
                    i = -1;
                  } else if (tempFormula[i + 2] === '-') {
                    tempAnswer = (parseFloat(tempFormula[i]) - parseFloat(tempFormula[i + 1]));
                    tempFormula[i] = tempAnswer;
                    tempFormula.splice(i + 1, 2);
                    i = -1;
                  } else if (tempFormula[i + 2] === '×') {
                    tempAnswer = (parseFloat(tempFormula[i]) * parseFloat(tempFormula[i + 1]));
                    tempFormula[i] = tempAnswer;
                    tempFormula.splice(i + 1, 2);
                    i = -1;
                  } else if (tempFormula[i + 2] === '÷') {
                    if (parseFloat(tempFormula[i + 1]) !== 0) {
                      tempAnswer = (parseFloat(tempFormula[i]) / parseFloat(tempFormula[i + 1]));
                      tempFormula[i] = tempAnswer;
                      tempFormula.splice(i + 1, 2);
                      i = -1;
                    } else {
                      return 'Error - divide by zero';                     
                    }
                  }
                }
              }
              answerOfSolve = tempFormula[0];
              return answerOfSolve;
            }
            outResult = toReversePolishNotation(inVal);
            if (outResult !== 'Error - incorrect formula') {
              result = solveReversePolishNotation(outResult);
            } else {
              result = 'Error - incorrect formula';
            }
              
        
        //result = 100; //eval( exp.replace(/÷/g, "/").replace(/×/g, "*") );
      }
      catch(err) {
        result = "ERROR";
      }
      return result;
    }
  }

  Calculator.prototype.addToDisplay = function(displayElement) {
    var calculator = this;
    $('.button.input').click(function() {
      var displayElementText = displayElement.text();
      var newChar = $(this).attr('data-value');
      var newExp;
      if ( calculator.emptyDisplay(displayElementText) ) {
        newExp = newChar;
      } else {
        if ( !$(this).hasClass('operation') || !calculator.displayLastCharOperator(displayElementText) ) {
          newExp = displayElementText.concat(newChar);
        }
      }
      displayElement.html(newExp);
    });
  }

  Calculator.prototype.clearDisplay = function(displayElement) {
    $('.button.clear').click(function() {
      displayElement.html('0');
    });
  }

  Calculator.prototype.removeCharFromDisplay = function(displayElement) {
    var calculator = this;
    $('.button.remove').click(function() {
      var displayElementText = displayElement.text();
      var newValue;
      if ( !calculator.emptyDisplay(displayElementText) ) {
        if (displayElementText.length > 1) {
          displayElement.html(displayElementText.slice(0, displayElementText.length - 1) );
        } else if (displayElementText.length === 1) {
          displayElement.html('0');
        }
      }
    });
  }

  Calculator.prototype.calculate = function(displayElement) {
    var calculator = this;
    $('.button.equal').click(function() {
      var displayElementText = displayElement.text();
      displayElement.html( calculator.evaluateExpression(displayElementText) );
    });
  }

  Calculator.prototype.memoryManage = function(displayElement) {
    var calculator = this;
    $('.button.memory-add').click(function() {
      var displayElementText = displayElement.text();
      var currentValue = calculator.evaluateExpression(displayElementText);
      if (( currentValue !== "ERROR") && (currentValue !== 'Error - incorrect formula') && (currentValue !== 'Error - divide by zero')) {
        if ( $(this).hasClass('add-plus') ) {
          calculator.$memory += parseFloat(currentValue);
        } else if ( $(this).hasClass('add-minus') ) {
          calculator.$memory -= parseFloat(currentValue);
        }
        $('.memory-val').html(calculator.$memory);
      }
    });
    $('.button.memory-clear').click(function() {
      calculator.$memory = 0;
      $('.memory-val').html(calculator.$memory);
    });
    $('.button.memory-return').click(function() {
	
		if(calculator.$memory!='0')
		var displayElementText = displayElement.text().concat(calculator.$memory);
	    displayElement.html(displayElementText);
		}
    );
  }
  
  

  Calculator.prototype.init = function() {
    this.addToDisplay(this.$displayElement);
    this.clearDisplay(this.$displayElement);
    this.removeCharFromDisplay(this.$displayElement);
    this.calculate(this.$displayElement);
    this.memoryManage(this.$displayElement);
  }

  $(document).ready(function() {
    var calculator = new Calculator();
    calculator.init();
  });


})(jQuery);
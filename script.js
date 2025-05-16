// DOM Elements
const resultDisplay = document.getElementById('result');
const historyDisplay = document.getElementById('history');
const historyPanel = document.getElementById('historyPanel');
const historyList = document.getElementById('historyList');
const historyToggle = document.getElementById('historyToggle');
const themeToggle = document.getElementById('themeToggle');
const clearBtn = document.getElementById('clear');
const clearEntryBtn = document.getElementById('clearEntry');
const squareBtn = document.getElementById('square');
const squareRootBtn = document.getElementById('squareRoot');
const cubeRootBtn = document.getElementById('cubeRoot');
const percentBtn = document.getElementById('percent');
const equalsBtn = document.getElementById('equals');
const clearHistoryBtn = document.getElementById('clearHistory');
const numberBtns = document.querySelectorAll('.number');
const operatorBtns = document.querySelectorAll('.operator');

// Variables
let firstOperand = null;
let secondOperand = null;
let currentOperator = null;
let shouldResetDisplay = false;
let calculationHistory = [];

// Initialize display
updateDisplay('0');

// Check for saved theme preference
if (localStorage.getItem('darkMode') === 'true') {
    document.body.classList.add('dark-mode');
    themeToggle.textContent = '☀️';
}

// Event Listeners
numberBtns.forEach(button => {
    button.addEventListener('click', () => {
        const digit = button.textContent;
        inputDigit(digit);
    });
});

operatorBtns.forEach(button => {
    button.addEventListener('click', () => {
        const operator = button.textContent;
        inputOperator(operator);
    });
});

clearBtn.addEventListener('click', clearAll);
clearEntryBtn.addEventListener('click', clearEntry);
equalsBtn.addEventListener('click', performCalculation);
squareBtn.addEventListener('click', square);
squareRootBtn.addEventListener('click', squareRoot);
cubeRootBtn.addEventListener('click', cubeRoot);
percentBtn.addEventListener('click', percentage);
clearHistoryBtn.addEventListener('click', clearHistory);

historyToggle.addEventListener('click', toggleHistory);
themeToggle.addEventListener('click', toggleTheme);

// Functions
function updateDisplay(value) {
    // Format large numbers with commas
    if (!isNaN(value) && typeof value === 'number') {
        let formattedValue = value.toString();
        if (formattedValue.includes('.')) {
            const parts = formattedValue.split('.');
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            formattedValue = parts.join('.');
        } else {
            formattedValue = formattedValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        }
        resultDisplay.textContent = formattedValue;
    } else {
        resultDisplay.textContent = value;
    }
}

function getCleanDisplayValue() {
    return parseFloat(resultDisplay.textContent.replace(/,/g, ''));
}

function updateHistory(value) {
    historyDisplay.textContent = value;
}

function inputDigit(digit) {
    const displayValue = resultDisplay.textContent.replace(/,/g, '');
    
    if (shouldResetDisplay) {
        updateDisplay(digit === '.' ? '0.' : digit);
        shouldResetDisplay = false;
    } else {
        // Check if already contains a decimal point
        if (digit === '.' && displayValue.includes('.')) return;
        
        updateDisplay(displayValue === '0' ? 
            (digit === '.' ? '0.' : digit) : 
            displayValue + digit);
    }
}

function inputOperator(operator) {
    const displayValue = getCleanDisplayValue();
    
    if (currentOperator && !shouldResetDisplay) {
        performCalculation();
    }
    
    firstOperand = displayValue;
    currentOperator = operator;
    updateHistory(`${displayValue} ${operator}`);
    shouldResetDisplay = true;
}

function performCalculation() {
    if (currentOperator === null) return;
    
    const displayValue = getCleanDisplayValue();
    secondOperand = displayValue;
    
    let result;
    const calculation = `${firstOperand} ${currentOperator} ${secondOperand}`;
    
    switch (currentOperator) {
        case '+':
            result = firstOperand + secondOperand;
            break;
        case '-':
            result = firstOperand - secondOperand;
            break;
        case '*':
            result = firstOperand * secondOperand;
            break;
        case '/':
            if (secondOperand === 0) {
                updateDisplay('Error');
                updateHistory('');
                firstOperand = null;
                secondOperand = null;
                currentOperator = null;
                shouldResetDisplay = true;
                return;
            }
            result = firstOperand / secondOperand;
            break;
        default:
            return;
    }
    
    // Round to avoid floating point issues
    result = Math.round(result * 1000000000) / 1000000000;
    
    updateDisplay(result);
    updateHistory(`${calculation} = ${result}`);
    
    // Add to history
    addToHistory(`${calculation} = ${result}`);
    
    firstOperand = result;
    currentOperator = null;
    shouldResetDisplay = true;
}

function clearAll() {
    updateDisplay('0');
    updateHistory('');
    firstOperand = null;
    secondOperand = null;
    currentOperator = null;
    shouldResetDisplay = false;
}

function clearEntry() {
    updateDisplay('0');
    shouldResetDisplay = false;
}

function square() {
    const displayValue = getCleanDisplayValue();
    const result = displayValue * displayValue;
    const calculation = `${displayValue}² = ${result}`;
    
    updateDisplay(result);
    updateHistory(calculation);
    
    addToHistory(calculation);
    shouldResetDisplay = true;
}

function squareRoot() {
    const displayValue = getCleanDisplayValue();
    
    if (displayValue < 0) {
        updateDisplay('Error');
        return;
    }
    
    const result = Math.sqrt(displayValue);
    const calculation = `√${displayValue} = ${result}`;
    
    updateDisplay(result);
    updateHistory(calculation);
    
    addToHistory(calculation);
    shouldResetDisplay = true;
}

function cubeRoot() {
    const displayValue = getCleanDisplayValue();
    const result = Math.cbrt(displayValue);
    const calculation = `∛${displayValue} = ${result}`;
    
    updateDisplay(result);
    updateHistory(calculation);
    
    addToHistory(calculation);
    shouldResetDisplay = true;
}

function percentage() {
    const displayValue = getCleanDisplayValue();
    
    if (firstOperand === null || currentOperator === null) {
        const result = displayValue / 100;
        updateDisplay(result);
        return;
    }
    
    // Calculate percentage based on the first operand
    const percentValue = (firstOperand * displayValue) / 100;
    updateDisplay(percentValue);
}

function addToHistory(entry) {
    calculationHistory.push(entry);
    renderHistory();
}

function renderHistory() {
    historyList.innerHTML = '';
    
    calculationHistory.forEach(entry => {
        const historyEntry = document.createElement('div');
        historyEntry.classList.add('history-entry');
        historyEntry.textContent = entry;
        historyList.appendChild(historyEntry);
    });
}

function clearHistory() {
    calculationHistory = [];
    renderHistory();
}

function toggleHistory() {
    historyPanel.classList.toggle('show');
}

function toggleTheme() {
    document.body.classList.toggle('dark-mode');
    themeToggle.textContent = document.body.classList.contains('dark-mode') ? '☀️' : '🌙';
    
    // Save theme preference
    localStorage.setItem('darkMode', document.body.classList.contains('dark-mode'));
}

// Keyboard support
document.addEventListener('keydown', event => {
    const key = event.key;
    
    if (/[0-9.]/.test(key)) {
        event.preventDefault();
        inputDigit(key);
    } else if (['+', '-', '*', '/'].includes(key)) {
        event.preventDefault();
        inputOperator(key);
    } else if (key === 'Enter' || key === '=') {
        event.preventDefault();
        performCalculation();
    } else if (key === 'Escape') {
        event.preventDefault();
        clearAll();
    } else if (key === 'Backspace') {
        event.preventDefault();
        const currentValue = resultDisplay.textContent.replace(/,/g, '');
        if (currentValue.length > 1) {
            updateDisplay(currentValue.slice(0, -1));
        } else {
            updateDisplay('0');
        }
    }
});
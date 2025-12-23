document.addEventListener('DOMContentLoaded', () => {
    const addTaskBtn = document.getElementById('addTaskBtn');
    const taskInput = document.getElementById('taskInput');
    const taskList = document.getElementById('taskList');

    addTaskBtn.addEventListener('click', () => {
        const task = taskInput.value.trim();
        if (task) {
            const li = document.createElement('li');
            li.className = 'flex justify-between items-center p-2 bg-gray-100 rounded';
            li.innerHTML = `\n                <span class="text-gray-700">${task}</span>\n                <button class="text-red-500 hover:text-red-700">删除</button>\n            `;
            taskList.appendChild(li);
            taskInput.value = '';
        }
    });

    taskList.addEventListener('click', (e) => {
        if (e.target.classList.contains('text-red-500')) {
            e.target.parentElement.remove();
        }
    });
});
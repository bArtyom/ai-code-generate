document.getElementById('addTaskBtn').addEventListener('click', () => {
      const taskInput = document.getElementById('taskInput');
      const taskText = taskInput.value.trim();
      if (taskText) {
        const taskList = document.getElementById('taskList');
        const li = document.createElement('li');
        li.className = 'p-2 bg-gray-100 rounded mb-2 flex justify-between items-center';
        li.innerHTML = `<span class="text-gray-700">${taskText}</span><button class="text-red-500 hover:text-red-700" onclick="removeTask(this)">删除</button>`;
        taskList.appendChild(li);
        taskInput.value = '';
      }
    });

    function removeTask(button) {
      const li = button.parentElement;
      li.remove();
    }
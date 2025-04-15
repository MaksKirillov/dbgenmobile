import subprocess
import sys

def execute_command(command):
    try:
        # Выполняем команду и получаем результат
        result = subprocess.run(command, shell=True, check=True, text=True, capture_output=True)
        print(result.stdout)
    except subprocess.CalledProcessError as e:
        print(e.stderr)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Использование: python script.py <команда>")
    else:
        command = ' '.join(sys.argv[1:])
        execute_command(command)
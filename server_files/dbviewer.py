import argparse
import textwrap
import pandas as pd
import os

if __name__ == '__main__':
    """Основное тело консольного приложения"""

    """Описание и аргументы консольного приложения."""
    parser = argparse.ArgumentParser(
        prog='dbviewer',
        description=textwrap.dedent('''\
                                        Просмотр таблиц в формате feather
                                        '''),
        formatter_class=argparse.RawDescriptionHelpFormatter)

    parser.add_argument('-i', '--input', type=str, help='Путь к таблице dataframe в формате .feather;')
    args = parser.parse_args()

    """Проверка аргументов"""

    """Проверка пути к таблице dataframe в формате .feather"""

    if args.input is None:
        raise FileNotFoundError('Не указан путь к таблице dataframe в формате .feather')
    else:
        try:
            file_input = args.input
            if os.path.isfile(file_input):
                if file_input.endswith('.feather'):
                    pass
                else:
                    raise TypeError('Файл должен быть формата .feather и оканчиваться на .feather')
            else:
                raise FileNotFoundError('Данного файла не существует')
        except Exception as error:
            print(f"Произошла ошибка: {error}")

    file_input = args.input

    """Чтение таблицы dataframe в формате .feather"""

    df = pd.read_feather(file_input)

    print(df.head(20).to_csv(sep='|', index=False))
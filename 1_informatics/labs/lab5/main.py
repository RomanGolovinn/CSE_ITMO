import pandas as pd

excel_file = "lab5_inf.ods"
df_raw = pd.read_excel(excel_file, header=None)

X = df_raw.iloc[1:13, 1].astype(int).reset_index(drop=True)

bits = (
    df_raw.iloc[1:13, 5:21]
    .fillna(0)
    .astype(int)
    .reset_index(drop=True)
)

A = int(df_raw.iloc[0, 1])
C = int(df_raw.iloc[0, 3])

print(f"A = {A}    C = {C}\n")


print("-" * 52)

for i in range(12):
    x_val = X[i]
    bit_str = "".join(f"{b}" for b in bits.iloc[i])
    print(f"X{i+1:<2} = {x_val:>6}   B{i+1:<2} = {bit_str}")


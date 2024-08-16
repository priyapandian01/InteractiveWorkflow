import streamlit as st
import pandas as pd
import matplotlib.pyplot as plt
import plotly.express as px

# Function to load inspection checklist
def load_checklist(file):
    return pd.read_csv(file)

# Function to visualize inspection results
def visualize_results(df):
    fig = px.bar(df, x='Area', y='Issues Found', color='Severity', title='Safety Inspection Results')
    st.plotly_chart(fig)

# Function to log new inspection
def log_inspection(area, issue, severity):
    new_entry = {'Area': area, 'Issues Found': issue, 'Severity': severity}
    return new_entry

# Streamlit App
def main():
    st.title("Safety Inspection Workflow Tool")

    st.sidebar.header("Upload Inspection Checklist")
    checklist_file = st.sidebar.file_uploader("Upload CSV", type=['csv'])

    if checklist_file is not None:
        df = load_checklist(checklist_file)
        st.write("### Inspection Checklist")
        st.dataframe(df)

        st.write("### Inspection Results")
        if st.sidebar.button("Visualize Results"):
            visualize_results(df)

    st.sidebar.header("Log New Inspection")
    area = st.sidebar.text_input("Area Inspected")
    issue = st.sidebar.text_input("Issues Found")
    severity = st.sidebar.selectbox("Severity Level", ['Low', 'Medium', 'High'])

    if st.sidebar.button("Log Inspection"):
        new_entry = log_inspection(area, issue, severity)
        df = df.append(new_entry, ignore_index=True)
        st.write("### Updated Inspection Checklist")
        st.dataframe(df)

        if st.sidebar.button("Save Checklist"):
            df.to_csv('updated_checklist.csv', index=False)
            st.sidebar.write("Checklist saved as 'updated_checklist.csv'.")

if _name_ == '_main_':
    main()
